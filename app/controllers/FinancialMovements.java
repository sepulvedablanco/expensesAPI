package controllers;

import com.avaje.ebean.PagedList;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import models.BankAccount;
import models.FinancialMovement;
import models.FinancialMovementSubtype;
import models.FinancialMovementType;
import models.User;
import helpers.ActionAuthenticator;
import helpers.Constants;
import helpers.ControllerHelper;
import helpers.Constants.Messages;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * 
 * @author Diego Sepulveda Blanco
 *
 */
@Security.Authenticated(ActionAuthenticator.class)
public class FinancialMovements extends Controller {

	/**
	 * Action method for GET /user/<uId>/financialMovements
	 * 
	 * @param uId user identifier
	 */
	public Result find(Long uId) {
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(110, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(111, Messages.User.TOKEN_ID_CONFLICT));
		}
		
    	String movementId = request().getQueryString(Constants.QueryString.ID);
    	Long fmId = movementId == null ? null : Long.parseLong(movementId);
    	if(fmId != null) {
    		return findOne(uId, fmId);
    	}
		
    	String concept = request().getQueryString(Constants.QueryString.CONCEPT);
    	String conceptLike = request().getQueryString(Constants.QueryString.CONCEPT_LIKE);
    	String expense = request().getQueryString(Constants.QueryString.EXPENSE);
    	Boolean bExpense = expense == null ? null : Boolean.parseBoolean(expense);
    	String type = request().getQueryString(Constants.QueryString.TYPE);
    	Long typeId = type == null ? null : Long.parseLong(type);
    	String typeDesc = request().getQueryString(Constants.QueryString.TYPE_DESCRIPTION);
    	String subtype = request().getQueryString(Constants.QueryString.SUBTYPE);
    	Long subtypeId = subtype == null ? null : Long.parseLong(subtype);
    	String subtypeDesc = request().getQueryString(Constants.QueryString.SUBTYPE_DESCRIPTION);
    	String bankAccount = request().getQueryString(Constants.QueryString.BANK_ACCOUNT);
    	Long bankAccountId = bankAccount == null ? null : Long.parseLong(bankAccount);
    	String baDesc = request().getQueryString(Constants.QueryString.BANK_ACCOUNT_DESCRIPTION);
    	String year = request().getQueryString(Constants.QueryString.YEAR);
    	Integer iYear = year == null ? null : Integer.parseInt(year);
    	String month = request().getQueryString(Constants.QueryString.MONTH);
    	Integer iMonth = month == null ? null : Integer.parseInt(month);
    	String limit = request().getQueryString(Constants.QueryString.LIMIT);
    	Integer iLimit = limit == null ? Constants.QueryString.DEFAULT_PAGE_SIZE : Integer.parseInt(limit);
    	String offset = request().getQueryString(Constants.QueryString.OFFSET);
    	Integer iOffset = offset == null ? 0 : Integer.parseInt(offset);
    	
    	PagedList<FinancialMovement> lstMovements = FinancialMovement.findPage(concept, conceptLike,
    			bExpense, uId, typeId, typeDesc, subtypeId, subtypeDesc, bankAccountId, baDesc,
    			iYear, iMonth, iLimit, iOffset);
		
		if (ControllerHelper.acceptsJson(request())) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(play.i18n.Messages.get(Constants.Messages.Common.PAGE_TOTAL_MOVEMENTS), lstMovements.getTotalRowCount());
			result.put(play.i18n.Messages.get(Constants.Messages.Common.PAGE), lstMovements.getList());
			return ok(Json.toJson(result));
		}

		if (ControllerHelper.acceptsXml(request())) {
			return ok(views.xml.financialMovements.render(lstMovements.getList(), lstMovements.getTotalRowCount()));
		}

		return badRequest(ControllerHelper.generateJsonResponse(112, Messages.Common.UNSUPPORTED_FORMAT));
	}
	
	/**
	 * Search for a single financial movement
	 * 
	 * @param uId user identifier
	 * @param fmId Financial movement identifier to search
	 *
	 */
	public Result findOne(Long uId, Long fmId) {
		FinancialMovement financialMovement = FinancialMovement.find.byId(fmId);
		
		if(financialMovement == null) {
			return notFound(ControllerHelper.generateJsonResponse(113, Messages.FinanMov.NOT_FOUND, fmId));
		}
		
		if(financialMovement.getUser().getId() != uId) {
			return badRequest(ControllerHelper.generateJsonResponse(114, Constants.Messages.FinanMov.INVALID_FINAN_MOV_USER));
		}
		
		if (ControllerHelper.acceptsJson(request())) {
			return ok(Json.toJson(financialMovement));
		}

		if (ControllerHelper.acceptsXml(request())) {
			return ok(views.xml._financialMovement.render(financialMovement));
		}

		return badRequest(ControllerHelper.generateJsonResponse(115, Messages.Common.UNSUPPORTED_FORMAT));
	}
	
	/**
	 * Action method for GET /user/<uId>/financialMovements/amounts
	 * 
	 * @param uId user identifier
	 */
	public Result amounts(Long uId) {
		User user = User.find.byId(uId);
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(116, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(117, Messages.User.TOKEN_ID_CONFLICT));
		}

    	String concept = request().getQueryString(Constants.QueryString.CONCEPT);
    	String conceptLike = request().getQueryString(Constants.QueryString.CONCEPT_LIKE);
    	String expense = request().getQueryString(Constants.QueryString.EXPENSE);
    	Boolean bExpense = expense == null ? null : Boolean.parseBoolean(expense);
    	String type = request().getQueryString(Constants.QueryString.TYPE);
    	Long typeId = type == null ? null : Long.parseLong(type);
    	String typeDesc = request().getQueryString(Constants.QueryString.TYPE_DESCRIPTION);
    	String subtype = request().getQueryString(Constants.QueryString.SUBTYPE);
    	Long subtypeId = subtype == null ? null : Long.parseLong(subtype);
    	String subtypeDesc = request().getQueryString(Constants.QueryString.SUBTYPE_DESCRIPTION);
    	String bankAccount = request().getQueryString(Constants.QueryString.BANK_ACCOUNT);
    	Long bankAccountId = bankAccount == null ? null : Long.parseLong(bankAccount);
    	String baDesc = request().getQueryString(Constants.QueryString.BANK_ACCOUNT_DESCRIPTION);
    	String year = request().getQueryString(Constants.QueryString.YEAR);
    	Integer iYear = year == null ? null : Integer.parseInt(year);
    	String month = request().getQueryString(Constants.QueryString.MONTH);
    	Integer iMonth = month == null ? null : Integer.parseInt(month);
    	
    	BigDecimal amount = FinancialMovement.calculateAmount(concept, conceptLike,
    			bExpense, uId, typeId, typeDesc, subtypeId, subtypeDesc, bankAccountId, 
    			baDesc, iYear, iMonth);
		
		return ok(ControllerHelper.format(request(), amount));
	}
	
	/**
	 * Action method for POST /user/<uId>/financialMovement.
	 * Bank account attributes must be pass in the body of the request
	 */
	public Result create(Long uId) {
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(118, Messages.User.NOT_FOUND, uId));
		}

		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(119, Messages.User.TOKEN_ID_CONFLICT));
		}

		Form<FinancialMovement> form = Form.form(FinancialMovement.class).bindFromRequest();
		if (form.hasErrors()) {
			System.out.println(form.errorsAsJson());
			return badRequest(ControllerHelper.errorToJson(120, Constants.Messages.FinanMov.INVALID, form.errorsAsJson()));
		}

		FinancialMovement financialMovement = form.get();
		
		Long fmtId = financialMovement.getFinancialMovementType().getId();
		FinancialMovementType financialMovementType = FinancialMovementType.find.byId(fmtId);
		if(financialMovementType == null) {
			return notFound(ControllerHelper.generateJsonResponse(121, Messages.FinanMovType.NOT_FOUND, fmtId));
		}		

		FinancialMovementSubtype financialMovementSubtype = null;
		if(financialMovement != null && financialMovement.getFinancialMovementSubtype() != null &&
				financialMovement.getFinancialMovementSubtype().getId() != null) {
			Long fmsId = financialMovement.getFinancialMovementSubtype().getId();
			financialMovementSubtype = FinancialMovementSubtype.find.byId(fmsId);
			if(financialMovementSubtype == null) {
				return notFound(ControllerHelper.generateJsonResponse(122, Messages.FinanMovSubtype.NOT_FOUND, fmsId));
			}
		}
		
		if(financialMovementSubtype != null && 
				financialMovementSubtype.getFinancialMovementType().getId() != financialMovementType.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(123, Constants.Messages.FinanMovSubtype.INVALID_FINAN_MOV_SUBTYPE_TYPE));
		}

		if(financialMovementType.getUser().getId() != user.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(124, Constants.Messages.FinanMovType.INVALID_FINAN_MOV_TYPE_USER));
		}

		Long baId = financialMovement.getBankAccount().getId();
		BankAccount bankAccount = BankAccount.find.byId(baId);
		
		if(bankAccount == null) {
			return notFound(ControllerHelper.generateJsonResponse(125, Messages.BankAccount.NOT_FOUND, baId));
		}
		
		if(bankAccount.getUser().getId() != user.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(126, Constants.Messages.BankAccount.INVALID_BANK_ACCOUNT_USER));
		}
		
		if(financialMovement.getExpense()) {
			bankAccount.setBalance(bankAccount.getBalance().subtract(financialMovement.getAmount()));
		} else {
			bankAccount.setBalance(bankAccount.getBalance().add(financialMovement.getAmount()));
		}
				
		bankAccount.update();

		financialMovement.setFinancialMovementType(financialMovementType);
		financialMovement.setFinancialMovementSubtype(financialMovementSubtype);
		financialMovement.setBankAccount(bankAccount);
		financialMovement.setUser(user);
				
		financialMovement.save();
		return created(ControllerHelper.generateJsonResponse(127, Messages.FinanMov.CREATED, financialMovement.getConcept()));
	}

	/**
	 * Action method for PUT /user/<uId>/financialMovement/<fmId>
	 * Financial movements attributes to be modified must be pass in the body of the request
	 * 
	 * @param fmId financial movement identifier to modify
	 */
	public Result update(Long uId, Long fmId) {
		return status(Http.Status.NOT_IMPLEMENTED, ControllerHelper.generateJsonResponse(128, Messages.FinanMov.UPDATED));
	}
	
	/**
	 * Action method for DELETE /user/<uId>/financialMovement/<fmId>
	 * 
	 * @param baId bank account identifier to delete
	 */
	public Result delete(Long uId, Long fmId) {
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(129, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(130, Messages.User.TOKEN_ID_CONFLICT));
		}

		FinancialMovement financialMovement = FinancialMovement.find.byId(fmId);
		if (financialMovement == null) {
			return notFound(ControllerHelper.generateJsonResponse(131, Messages.FinanMov.NOT_FOUND, fmId));
		}
		
		if (financialMovement.getUser().getId() != user.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(132, Constants.Messages.FinanMov.INVALID_FINAN_MOV_USER));
		}
		
		BankAccount bankAccount = financialMovement.getBankAccount();
		if(financialMovement.getExpense()) {
			bankAccount.setBalance(bankAccount.getBalance().add(financialMovement.getAmount()));			
		} else {
			bankAccount.setBalance(bankAccount.getBalance().subtract(financialMovement.getAmount()));						
		}

		financialMovement.delete();
		bankAccount.update();
		
		return ok(ControllerHelper.generateJsonResponse(133, Messages.FinanMov.DELETED));
	}
		
}
