package models;

import helpers.Constants;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Digits;

import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class FinancialMovement extends Model {
	
	@Id
	@SequenceGenerator(name=Constants.Sequences.FINAN_MOV_GEN, sequenceName=Constants.Sequences.FINAN_MOV, initialValue=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=Constants.Sequences.FINAN_MOV_GEN)
	private Long id;
	
	@Required
	public Boolean expense;
	
	@Required
	public String concept;
	
	@DateTime(pattern=Constants.DATE_FORMAT)
	@JsonFormat(pattern=Constants.DATE_FORMAT)
	public Date transactionDate;

	@Required
	@Column(nullable=false, precision=18, scale=2)
	@Digits(integer = 20, fraction = 2)
	public BigDecimal amount;

	@Required
	@ManyToOne
	public FinancialMovementType financialMovementType;

	@ManyToOne
	public FinancialMovementSubtype financialMovementSubtype;	
		
	@Required
	@ManyToOne
	private BankAccount bankAccount;

	@ManyToOne
	@JsonIgnore
	private User user;

	public static final Find<Long, FinancialMovement> find = new Find<Long, FinancialMovement>(){};

	public static DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);

	public FinancialMovement() {}
	
	@PrePersist
	@PreUpdate
	public void fillData() {
		if (transactionDate == null) {
			transactionDate = new Date();
		}
	}
	
	public static PagedList<FinancialMovement> findPage(String concept, String conceptLike,
			Boolean expense, Long user, Long type, String typeDesc, Long subtype, String subtypeDesc,
			Long bankAccount, String bankAccountDesc, Integer year, Integer month,
			Integer iLimit, Integer iOffset) {
		ExpressionList<FinancialMovement> query = getExpressionList(concept, conceptLike,
				expense, user, type, typeDesc, subtype, subtypeDesc, bankAccount, 
				bankAccountDesc, year, month, false);
		return query.findPagedList(iLimit, iOffset);
	}
	
	public static BigDecimal calculateAmount(String concept, String conceptLike,
			Boolean expense, Long user, Long type, String typeDesc, Long subtype, String subtypeDesc,
			Long bankAccount, String bankAccountDesc, Integer year, Integer month) {
		
		ExpressionList<FinancialMovement> query = getExpressionList(concept, conceptLike,
				expense, user, type, typeDesc, subtype, subtypeDesc, bankAccount, 
				bankAccountDesc, year, month, true);
		
		String sql = " select sum(amount) as amount"
				   + " from financial_movement t0"
				   + " inner join financial_movement_type t1 on (t0.financial_movement_type_id = t1.id)"
				   + " inner join financial_movement_subtype t2 on (t1.id = t2.financial_movement_type_id and t0.financial_movement_subtype_id = t2.id)"
				   + " inner join bank_account t3 on (t0.bank_account_id = t3.id)";

		RawSql rawSql = RawSqlBuilder.parse(sql).create();

		FinancialMovement finanMovAmount = Ebean.find(FinancialMovement.class)
				.setRawSql(rawSql)
				.where().addAll(query).findUnique();
		return finanMovAmount == null || finanMovAmount.getAmount() == null ? BigDecimal.ZERO : finanMovAmount.getAmount();
	}
	
	public static ExpressionList<FinancialMovement> getExpressionList(String concept, String conceptLike,
			Boolean expense, Long user, Long type, String typeDesc, Long subtype, String subtypeDesc,
			Long bankAccount, String bankAccountDesc, Integer year, Integer month, boolean amount) {
		
		ExpressionList<FinancialMovement> query = find.orderBy("id desc").where()
				.eq(amount ? "t0.user_id" : "user.id", user);
		if(concept != null) {
			query.eq("concept", concept);
		}
		if(conceptLike != null) {
			query.like("concept", "%" + conceptLike + "%");
		}
		if(expense != null) {
			query.eq("expense", expense);
		} else if(amount) { // if we are doing query about amounts we show amount of expenses by default.
			query.eq("expense", true);
		}
		if(type != null) {
			query.eq(amount ? "t0.financial_movement_type_id" : "financialMovementType.id", type);
		}
		if(typeDesc != null) {
			query.eq(amount ? "t1.description" : "financialMovementType.description", typeDesc);			
		}
		if(subtype != null) {
			query.eq(amount ? "t0.financial_movement_subtype_id" : "financialMovementSubtype.id", subtype);			
		}
		if(subtypeDesc != null) {
			query.eq(amount ? "t2.description" : "financialMovementSubtype.description", subtypeDesc);			
		}
		if(bankAccount != null) {
			query.eq(amount ? "bank_account_id" : "bankAccount.id", bankAccount);			
		}
		if(bankAccountDesc != null) {
			query.eq(amount ? "t3.description" : "bankAccount.description", bankAccountDesc);			
		}
		
		if(year == null && month == null) {
			return query;
		}
		
		Calendar cSince = Calendar.getInstance();
		Calendar cUntil = Calendar.getInstance();

		if(year != null) {
			cSince.set(Calendar.YEAR, year);
			cUntil.set(Calendar.YEAR, year);
		} // this year by default
		
		if(month != null) {
			cSince.set(Calendar.MONTH, month - 1);
			cUntil.set(Calendar.MONTH, month - 1);
		} else {
			cSince.set(Calendar.MONTH, 0);
			cUntil.set(Calendar.MONTH, 11);			
		}

		cSince.set(Calendar.DAY_OF_MONTH, 1);
		cUntil.set(Calendar.DAY_OF_MONTH, cUntil.getActualMaximum(Calendar.DAY_OF_MONTH));

        cSince.set(Calendar.HOUR_OF_DAY, 0);
        cSince.set(Calendar.MINUTE, 0);
        cSince.set(Calendar.SECOND, 0);
        
        cUntil.set(Calendar.HOUR_OF_DAY, 23);
        cUntil.set(Calendar.MINUTE, 59);
        cUntil.set(Calendar.SECOND, 59);

        System.out.println(cSince);
        System.out.println(cUntil);
        
		query.between(amount ? "transaction_date" : "transactionDate", cSince.getTime(), cUntil.getTime());

		return query;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getExpense() {
		return expense;
	}

	public void setExpense(Boolean expense) {
		this.expense = expense;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public FinancialMovementType getFinancialMovementType() {
		return financialMovementType;
	}

	public void setFinancialMovementType(FinancialMovementType financialMovementType) {
		this.financialMovementType = financialMovementType;
	}

	public FinancialMovementSubtype getFinancialMovementSubtype() {
		return financialMovementSubtype;
	}

	public void setFinancialMovementSubtype(
			FinancialMovementSubtype financialMovementSubtype) {
		this.financialMovementSubtype = financialMovementSubtype;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
