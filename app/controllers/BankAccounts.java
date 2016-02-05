package controllers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import helpers.ActionAuthenticator;
import helpers.Constants;
import helpers.ControllerHelper;
import helpers.Constants.Messages;
import models.BankAccount;
import models.User;
import play.api.i18n.Lang;
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
public class BankAccounts extends Controller {

	/**
	 * Action method para GET /user/<uId>/bankAccounts
	 * 
	 * @param uId user identifier
	 */
	public Result find(Long uId) {		
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(20, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(21, Messages.User.TOKEN_ID_CONFLICT));
		}
		
    	String bankAccountId = request().getQueryString(Constants.QueryString.ID);
    	Long baId = bankAccountId == null ? null : Long.parseLong(bankAccountId);
    	if(baId != null) {
    		return findOne(uId, baId);
    	}

		if (ControllerHelper.acceptsJson(request())) {
			return ok(Json.toJson(user.getBankAccounts()));
		}

		if (ControllerHelper.acceptsXml(request())) {
			return ok(views.xml.bankAccounts.render(user.getBankAccounts()));
		}

		return badRequest(ControllerHelper.generateJsonResponse(22, Messages.Common.UNSUPPORTED_FORMAT));
	}
	
	/**
	 * Search for a single bank account
	 * 
	 * @param uId user identifier
	 * @param baId bank account identifier to search
	 *
	 */
	public Result findOne(Long uId, Long baId) {
		BankAccount bankAccount = BankAccount.find.byId(baId);
		
		if(bankAccount == null) {
			return notFound(ControllerHelper.generateJsonResponse(23, Messages.BankAccount.NOT_FOUND, baId));
		}
		
		if(bankAccount.getUser().getId() != uId) {
			return badRequest(ControllerHelper.generateJsonResponse(24, Constants.Messages.BankAccount.INVALID_BANK_ACCOUNT_USER));
		}
				
		if (ControllerHelper.acceptsJson(request())) {
			return ok(Json.toJson(bankAccount));
		}

		if (ControllerHelper.acceptsXml(request())) {
			return ok(views.xml._bankAccount.render(bankAccount));
		}

		return badRequest(ControllerHelper.generateJsonResponse(25, Messages.Common.UNSUPPORTED_FORMAT));
	}
	
	/**
	 * Action method for POST /user/<uId>/bankAccount.
	 * Bank account attributes must be pass in the body of the request
	 * 
	 * @param uId user identifier
	 * 
	 */
	public Result create(Long uId) {
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(26, Messages.User.NOT_FOUND, uId));
		}

		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(27, Messages.User.TOKEN_ID_CONFLICT));
		}
		
		Form<BankAccount> form = Form.form(BankAccount.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorToJson(28, Constants.Messages.BankAccount.INVALID, form.errorsAsJson()));
		}

		BankAccount bankAccount = form.get();
		if(bankAccount.exist(null, uId)) {
			return status(Http.Status.CONFLICT, ControllerHelper.generateJsonResponse(26, Messages.BankAccount.DUPLICATE, bankAccount.getCompleteBankAccount()));
		}
		
		if(!bankAccount.isValid()) {
			return badRequest(ControllerHelper.generateJsonResponse(29, Messages.BankAccount.INVALID));
		}
		
		bankAccount.setUser(user);
		bankAccount.save();
		return created(ControllerHelper.generateJsonResponse(30, Messages.BankAccount.CREATED, bankAccount.getCompleteBankAccount()));
	}
	
	/**
	 * Action method for PUT /user/<uId>/bankAccount/<baId>
	 * Bank account attributes to be modified must be pass in the body of the request
	 * 
	 * @param uId user identifier
	 * @param baId bank account identifier to modify
	 */
	public Result update(Long uId, Long baId) {		
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(31, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(32, Messages.User.TOKEN_ID_CONFLICT));
		}
				
		Form<BankAccount> form = Form.form(BankAccount.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorToJson(33, Constants.Messages.BankAccount.INVALID, form.errorsAsJson()));
		}
		
		BankAccount bankAccount = BankAccount.find.byId(baId);
		if(bankAccount == null) {
			return notFound(ControllerHelper.generateJsonResponse(34, Messages.BankAccount.NOT_FOUND, baId));
		}
		
		if(bankAccount.getUser().getId() != user.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(35, Constants.Messages.BankAccount.INVALID_BANK_ACCOUNT_USER));
		}
		
		BankAccount bankAccountMod = form.get();
		if(bankAccountMod.exist(bankAccount.getId(), uId)) {
			return status(Http.Status.CONFLICT, ControllerHelper.generateJsonResponse(36, Messages.BankAccount.DUPLICATE, bankAccount.getCompleteBankAccount()));
		}
		
		if (!bankAccount.changeData(bankAccountMod)) {
			return status(Http.Status.NOT_MODIFIED, ControllerHelper.generateJsonResponse(37, Messages.BankAccount.NOT_MODIFIED));
		}
		
		if(!bankAccount.isValid()) {
			return badRequest(ControllerHelper.generateJsonResponse(38, Messages.BankAccount.INVALID));
		}

		bankAccount.update();
		return ok(ControllerHelper.generateJsonResponse(39, Messages.BankAccount.UPDATED));
	}


	/**
	 * Action method for DELETE /user/<uId>/bankAccount/<baId>
	 * 
	 * @param baId bank account identifier to delete
	 */
	public Result delete(Long uId, Long baId) {
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(40, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(41, Messages.User.TOKEN_ID_CONFLICT));
		}
		
		BankAccount bankAccount = BankAccount.find.byId(baId);
		if (bankAccount == null) {
			return notFound(ControllerHelper.generateJsonResponse(42, Messages.BankAccount.NOT_FOUND, baId));
		}
		
		if (bankAccount.getUser().getId() != user.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(43, Constants.Messages.BankAccount.INVALID_BANK_ACCOUNT_USER));
		}

		bankAccount.delete();
		return ok(ControllerHelper.generateJsonResponse(44, Messages.BankAccount.DELETED));
	}
	
	/**
	 * Action method for GET /user/<uId>/bankAccount/amount.
	 * 
	 * @param uId user identifier
	 *
	 */
	public Result amount(Long uId) {
		User user = User.find.byId(uId);
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(45, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(46, Messages.User.TOKEN_ID_CONFLICT));
		}

		return ok(ControllerHelper.format(request(), BankAccount.getAmount(uId)));
	}
}
