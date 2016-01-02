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
public class FinancialMovementSubtype extends Model {

	@Id
	@SequenceGenerator(name=Constants.Sequences.FINAN_MOV_SUBTYPE_GEN, sequenceName=Constants.Sequences.FINAN_MOV_SUBTYPE, initialValue=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator=Constants.Sequences.FINAN_MOV_SUBTYPE_GEN)
	private Long id;
		
	@Required
	public String description;
		
	@JsonIgnore
	@ManyToOne
	private FinancialMovementType financialMovementType;

	@JsonIgnore
	@OneToMany(cascade=CascadeType.ALL, mappedBy="financialMovementSubtype")
	@Valid
	private List<FinancialMovement> financialMovements = new ArrayList<FinancialMovement>();

	public static final Find<Long, FinancialMovementSubtype> find = new Find<Long, FinancialMovementSubtype>(){};

	public FinancialMovementSubtype() {}

	public boolean exist(Long id, Long fmtId) {
		ExpressionList<FinancialMovementSubtype> expression = find.query().orderBy("id").where()
				.eq("description", description).eq("financialMovementType.id", fmtId);
		if (id != null) {
			expression.ne("id", id);
		}
		int rowCount = expression.findRowCount();
		return rowCount > 0;
	}
	
	public boolean changeData(FinancialMovementSubtype financialMovementSubtype) {
		boolean changed = false;
		
		if (financialMovementSubtype.description != null) {
			this.description = financialMovementSubtype.description;
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

	public FinancialMovementType getFinancialMovementType() {
		return financialMovementType;
	}

	public void setFinancialMovementType(FinancialMovementType financialMovementType) {
		this.financialMovementType = financialMovementType;
	}

	public List<FinancialMovement> getFinancialMovements() {
		return financialMovements;
	}

	public void setFinancialMovements(List<FinancialMovement> financialMovements) {
		this.financialMovements = financialMovements;
	}

}
