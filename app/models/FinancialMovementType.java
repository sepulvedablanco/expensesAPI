package models;

import helpers.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.Valid;

import play.data.validation.Constraints.Required;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class FinancialMovementType extends Model {

	@Id
	@SequenceGenerator(name=Constants.Sequences.FINAN_MOV_TYPE_GEN, sequenceName=Constants.Sequences.FINAN_MOV_TYPE, initialValue=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=Constants.Sequences.FINAN_MOV_TYPE_GEN)
	private Long id;
		
	@Required
	public String description;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="financialMovementType")
	@Valid
	private List<FinancialMovementSubtype> financialMovementSubtypes = new ArrayList<FinancialMovementSubtype>();

	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="financialMovementType")
	@Valid
	private List<FinancialMovement> financialMovements = new ArrayList<FinancialMovement>();

	public static final Find<Long, FinancialMovementType> find = new Find<Long, FinancialMovementType>(){};

	public FinancialMovementType() {}

	public boolean exist(Long id, Long userId) {
		ExpressionList<FinancialMovementType> expression = find.query().orderBy("id").where()
				.eq("description", description).eq("user.id", userId);
		if (id != null) {
			expression.ne("id", id);
		}
		int rowCount = expression.findRowCount();
		return rowCount > 0;
	}
	
	public boolean changeData(FinancialMovementType newFinancialMovementKind) {
		boolean changed = false;
		
		if (newFinancialMovementKind.description != null) {
			this.description = newFinancialMovementKind.description;
			changed = true;
		}
		
		return changed;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<FinancialMovementSubtype> getFinancialMovementSubtypes() {
		return financialMovementSubtypes;
	}

	public void setFinancialMovementSubtypes(
			List<FinancialMovementSubtype> financialMovementSubtypes) {
		this.financialMovementSubtypes = financialMovementSubtypes;
	}

	public List<FinancialMovement> getFinancialMovements() {
		return financialMovements;
	}

	public void setFinancialMovements(List<FinancialMovement> financialMovements) {
		this.financialMovements = financialMovements;
	}

}
