package models;

import helpers.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import play.data.validation.Constraints.Required;
import validators.Password;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name=Constants.CustomTableNames.USER)
public class User extends Model {

	@Id
	@SequenceGenerator(name=Constants.Sequences.USER_GEN, sequenceName=Constants.Sequences.USER, initialValue=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=Constants.Sequences.USER_GEN)
	private Long id;

	private String authToken;
	
	@Required
    @Column(name=Constants.CustomColumnNames.USER_USERNAME, nullable = false)
	private String user;

	@JsonIgnore
	@Required
	@Password
	private String pass;

	@Required
	private String name;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="user")
	@Valid
	private List<BankAccount> bankAccounts = new ArrayList<BankAccount>();	
    
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="user")
	@Valid
	private List<FinancialMovementType> financialMovementTypes = new ArrayList<FinancialMovementType>();	

	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="user")
	@Valid
	private List<FinancialMovement> financialMovements = new ArrayList<FinancialMovement>();	

	public static final Find<Long, User> find = new Find<Long, User>(){};
	
	public User() {}
	
	public User(String user, String pass) {
		this(user, pass, null);
	}
	
	public User(String user, String pass, String name) {
		this.user = user;
		this.pass = pass;
		this.name = name;
	}
	
	public boolean existSomeUserWithThisUserName(String user) {
		List<User> users = find.where().eq("user", user).ne("id", id).findList();
		return users != null && !users.isEmpty();
	}
	  
    @PrePersist
    @PreUpdate
    public void hashPassword() 
    		throws NoSuchAlgorithmException, UnsupportedEncodingException {
       MessageDigest digest = MessageDigest.getInstance("SHA-1");
       digest.reset();
       byte[] input = digest.digest(pass.getBytes("UTF-8"));
       this.pass = Base64.encodeBase64String(input);
    }
	
    @PrePersist
    @PreUpdate
    public void generateAuthToken(){
    	this.authToken = UUID.randomUUID().toString();
	    //this.authToken = Base64.encodeBase64String(pass.getBytes());
	}
    
    /**
     * Field id must be generated automatically. If someone put the id in the POST body 
     * request these id doesn't have to be valid.
     */
    @PrePersist
    public void cleanAutogeneratedFields(){
//	    this.id = null;
	}
	
	public boolean changeData(User newUser) {
		boolean changed = false;
		
		if (newUser.user != null) {
			this.user = newUser.user;
			changed = true;
		}

		if (newUser.pass != null) {
			this.pass = newUser.pass;
			changed = true;
		}

		if (newUser.name != null) {
			this.name = newUser.name;
			changed = true;
		}
				
		return changed;
	}
	
	public User login() {
		try {
			hashPassword();
		} catch (Exception e) {
			return null;
		}
		return find.where().eq("user", user).eq("pass", pass).findUnique();
	}
	
	public static User getUserFromToken(String token) {
		return find.where().eq("authToken", token).findUnique();
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAuthToken() {
		return authToken;
	}


	public void setAuthToken(String token) {
		this.authToken = token;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPass() {
		return pass;
	}


	public void setPass(String pass) {
		this.pass = pass;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}
	
	public List<FinancialMovementType> getFinancialMovementTypes() {
		return financialMovementTypes;
	}

	public void setFinancialMovementTypes(
			List<FinancialMovementType> financialMovementTypes) {
		this.financialMovementTypes = financialMovementTypes;
	}

	public List<FinancialMovement> getFinancialMovements() {
		return financialMovements;
	}

	public void setFinancialMovements(List<FinancialMovement> financialMovements) {
		this.financialMovements = financialMovements;
	}
	
}