package models;

import helpers.Constants;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.validation.Valid;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.Length;

import play.data.validation.Constraints.Required;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.validator.routines.IBANValidator;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

@Entity
public class BankAccount extends Model {

	@Id
	@SequenceGenerator(name=Constants.Sequences.BANK_ACCOUNT_GEN, sequenceName=Constants.Sequences.BANK_ACCOUNT, initialValue=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=Constants.Sequences.BANK_ACCOUNT_GEN)
	private Long id;

	public String iban;

	@Required
	@Digits(integer = 4, fraction = 0)
	@Length(min = 4, max = 4)
	public String entity;

	@Required
	@Digits(integer = 4, fraction = 0)
	@Length(min = 4, max = 4)
	public String office;

	@Required
	@Digits(integer = 2, fraction = 0)
	@Length(min = 2, max = 2)
	public String controlDigit;

	@Required
	@Digits(integer = 10, fraction = 0)
	@Length(min = 10, max = 10)
	public String accountNumber;

	@Required
	@Column(nullable=false, precision=18, scale=2)
	@Digits(integer = 20, fraction = 2)
	public BigDecimal balance;

	@Required
	public String description;

	@ManyToOne
	@JsonIgnore
	private User user;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bankAccount")
	@Valid
	private List<FinancialMovement> financialMovements = new ArrayList<FinancialMovement>();

	public static final Find<Long, BankAccount> find = new Find<Long, BankAccount>() {};

	public BankAccount() {}

	@PrePersist
	@PreUpdate
	public void checkIban() {
		if (iban != null) {
			return;
		}

		Iban iban = new Iban.Builder().countryCode(CountryCode.ES)
				.bankCode(entity).branchCode(office)
				.nationalCheckDigit(controlDigit).accountNumber(accountNumber)
				.build();

		this.iban = CountryCode.ES.name() + iban.getCheckDigit();
	}

	public boolean exist(Long id, Long userId) {
		ExpressionList<BankAccount> expression = find.query().orderBy("id").where()
				.eq("iban", iban).eq("entity", entity).eq("office", office)
				.eq("controlDigit", controlDigit)
				.eq("accountNumber", accountNumber)
				.eq("user.id", userId);
		if (id != null) {
			expression.ne("id", id);
		}
		int rowCount = expression.findRowCount();
		return rowCount > 0;
	}

	public boolean changeData(BankAccount newBankAccount) {
		boolean changed = false;

		if (newBankAccount.entity != null) {
			this.entity = newBankAccount.entity;
			changed = true;
		}

		if (newBankAccount.office != null) {
			this.office = newBankAccount.office;
			changed = true;
		}

		if (newBankAccount.controlDigit != null) {
			this.controlDigit = newBankAccount.controlDigit;
			changed = true;
		}

		if (newBankAccount.accountNumber != null) {
			this.accountNumber = newBankAccount.accountNumber;
			changed = true;
		}

		if (newBankAccount.balance != null) {
			this.balance = newBankAccount.balance;
			changed = true;
		}

		if (newBankAccount.description != null) {
			this.description = newBankAccount.description;
			changed = true;
		}
		
		if(changed) {
			this.iban = newBankAccount.iban;
		}

		return changed;
	}

	@JsonIgnore
	public boolean isValid() {
		if(entity == null || office == null || controlDigit == null || accountNumber == null) {
			return false;
		}
		
		int digit1 = getControlDigit1();
		int digit2 = getControlDigit2();
		String checkDigitCalculated = String.valueOf(digit1).concat(
				String.valueOf(digit2));
		if (!checkDigitCalculated.equals(controlDigit)) {
			return false;
		}

		if (iban == null) {
			return true;
		}

		return IBANValidator.getInstance().isValid(
				iban + entity + office + controlDigit + accountNumber);
	}

	private int getControlDigit1() {
		int ep1 = Character.getNumericValue(entity.charAt(0)) * 4;
		int ep2 = Character.getNumericValue(entity.charAt(1)) * 8;
		int ep3 = Character.getNumericValue(entity.charAt(2)) * 5;
		int ep4 = Character.getNumericValue(entity.charAt(3)) * 10;
		int sumEntity = ep1 + ep2 + ep3 + ep4;

		int op1 = Character.getNumericValue(office.charAt(0)) * 9;
		int op2 = Character.getNumericValue(office.charAt(1)) * 7;
		int op3 = Character.getNumericValue(office.charAt(2)) * 3;
		int op4 = Character.getNumericValue(office.charAt(3)) * 6;
		int sumOffice = op1 + op2 + op3 + op4;

		int sum = sumEntity + sumOffice;
		int mod = sum % 11;
		int digit1 = 11 - mod;
		if(digit1 == 10) {
			return 1;
		}
		if (digit1 == 11) {
			return 0;
		}
		return digit1;
	}

	private int getControlDigit2() {
		int anp1 = Character.getNumericValue(accountNumber.charAt(0)) * 1;
		int anp2 = Character.getNumericValue(accountNumber.charAt(1)) * 2;
		int anp3 = Character.getNumericValue(accountNumber.charAt(2)) * 4;
		int anp4 = Character.getNumericValue(accountNumber.charAt(3)) * 8;
		int anp5 = Character.getNumericValue(accountNumber.charAt(4)) * 5;
		int anp6 = Character.getNumericValue(accountNumber.charAt(5)) * 10;
		int anp7 = Character.getNumericValue(accountNumber.charAt(6)) * 9;
		int anp8 = Character.getNumericValue(accountNumber.charAt(7)) * 7;
		int anp9 = Character.getNumericValue(accountNumber.charAt(8)) * 3;
		int anp10 = Character.getNumericValue(accountNumber.charAt(9)) * 6;
		int sum = anp1 + anp2 + anp3 + anp4 + anp5 + anp6 + anp7 + anp8 + anp9 + anp10;
		int mod = sum % 11;
		int digit2 = 11 - mod;
		if(digit2 == 10) {
			return 1;
		}
		if (digit2 == 11) {
			return 0;
		}
		return digit2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getControlDigit() {
		return controlDigit;
	}

	public void setControlDigit(String controlDigit) {
		this.controlDigit = controlDigit;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCompleteBankAccount() {
		return iban + entity + office + controlDigit + accountNumber;
	}

	public List<FinancialMovement> getFinancialMovements() {
		return financialMovements;
	}

	public void setFinancialMovements(List<FinancialMovement> financialMovements) {
		this.financialMovements = financialMovements;
	}

}
