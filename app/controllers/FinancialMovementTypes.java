package controllers;

import helpers.ActionAuthenticator;
import helpers.Constants;
import helpers.ControllerHelper;
import helpers.Constants.Messages;
import models.FinancialMovementType;
import models.User;
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
public class FinancialMovementTypes extends Controller {

	/**
	 * Action method for GET /user/<uId>/financialMovementTypes
	 * 
	 * @param uId user identifier
	 */
	public Result find(Long uId) {
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(50, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(51, Messages.User.TOKEN_ID_CONFLICT));
		}
		
    	String typeId = request().getQueryString(Constants.QueryString.ID);
    	Long fmtId = typeId == null ? null : Long.parseLong(typeId);
    	if(fmtId != null) {
    		return findOne(uId, fmtId);
    	}
		
		if (ControllerHelper.acceptsJson(request())) {
			return ok(Json.toJson(user.getFinancialMovementTypes()));
		}		

		if (ControllerHelper.acceptsXml(request())) {
			return ok(views.xml.financialMovementTypes.render(user.getFinancialMovementTypes()));
		}

		return badRequest(ControllerHelper.generateJsonResponse(52, Messages.Common.UNSUPPORTED_FORMAT));
	}
	
	/**
	 * Search for a single financial movement type
	 * 
	 * @param uId user identifier
	 * @param fmtId Financial movement type identifier to search
	 *
	 */
	public Result findOne(Long uId, Long fmtId) {
		FinancialMovementType financialMovementType = FinancialMovementType.find.byId(fmtId);
		
		if(financialMovementType == null) {
			return notFound(ControllerHelper.generateJsonResponse(53, Messages.FinanMovType.NOT_FOUND, fmtId));
		}
		
		if(financialMovementType.getUser().getId() != uId) {
			return badRequest(ControllerHelper.generateJsonResponse(54, Constants.Messages.FinanMovType.INVALID_FINAN_MOV_TYPE_USER));
		}
		
		if (ControllerHelper.acceptsJson(request())) {
			return ok(Json.toJson(financialMovementType));
		}

		if (ControllerHelper.acceptsXml(request())) {
			return ok(views.xml._financialMovementType.render(financialMovementType));
		}

		return badRequest(ControllerHelper.generateJsonResponse(55, Messages.Common.UNSUPPORTED_FORMAT));
	}

	
	/**
	 * Action method for POST /user/<uId>/financialMovementType.
	 * Financial movement Type attributes must be pass in the body of the request
	 * 
	 * @param uId user identifier
	 * 
	 */
	public Result create(Long uId) {
		User user = User.find.byId(uId);
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(56, Messages.User.NOT_FOUND, uId));
		}
				
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(57, Messages.User.TOKEN_ID_CONFLICT));
		}
		
		Form<FinancialMovementType> form = Form.form(FinancialMovementType.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorToJson(58, Constants.Messages.FinanMovType.INVALID, form.errorsAsJson()));
		}

		FinancialMovementType financialMovementType = form.get();
		financialMovementType.setUser(user);

		if(financialMovementType.exist(null, uId)) {
			return status(Http.Status.CONFLICT, ControllerHelper.generateJsonResponse(59, Constants.Messages.FinanMovType.DUPLICATE, financialMovementType.getDescription()));
		}
				
		financialMovementType.save();
		return created(ControllerHelper.generateJsonResponse(60, Messages.FinanMovType.CREATED, financialMovementType.getDescription()));
	}
	
	/**
	 * Action method for PUT /user/<uId>/financialMovementType/<fmtId>
	 * Financial movement Type attributes to be modified must be pass in the body of the request
	 * 
	 * @param uId user identifier
	 * @param fmtId Financial movement type identifier to modify
	 */
	public Result update(Long uId, Long fmtId) {
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(61, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(62, Messages.User.TOKEN_ID_CONFLICT));
		}

		Form<FinancialMovementType> form = Form.form(FinancialMovementType.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorToJson(63, Constants.Messages.FinanMovType.INVALID, form.errorsAsJson()));
		}
		
		FinancialMovementType financialMovementType = FinancialMovementType.find.byId(fmtId);
		if(financialMovementType == null) {
			return notFound(ControllerHelper.generateJsonResponse(64, Messages.FinanMovType.NOT_FOUND, fmtId));
		}
		
		if(financialMovementType.getUser().getId() != user.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(65, Constants.Messages.FinanMovType.INVALID_FINAN_MOV_TYPE_USER));
		}

		FinancialMovementType financialMovementTypeMod = form.get();
		if(financialMovementTypeMod.exist(financialMovementType.getId(), uId)) {
			return status(Http.Status.CONFLICT, ControllerHelper.generateJsonResponse(63, Constants.Messages.FinanMovType.DUPLICATE, financialMovementType.getDescription()));
		}
		
		if (!financialMovementType.changeData(financialMovementTypeMod)) {
			return status(Http.Status.NOT_MODIFIED, ControllerHelper.generateJsonResponse(64, Constants.Messages.FinanMovType.NOT_MODIFIED));
		}
		
		financialMovementType.update();
		return ok(ControllerHelper.generateJsonResponse(66, Messages.FinanMovType.UPDATED));
	}
	

	/**
	 * Action method for DELETE /user/<uId>/financialMovementType/<fmtId>
	 * 
	 * @param uId user identifier
	 * @param fmtId Financial movement type identifier to delete
	 */
	public Result delete(Long uId, Long fmtId) {
		User user = User.find.byId(uId);		
		if (user == null) {
			return notFound(ControllerHelper.generateJsonResponse(67, Messages.User.NOT_FOUND, uId));
		}
		
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(68, Messages.User.TOKEN_ID_CONFLICT));
		}

		FinancialMovementType financialMovementType = FinancialMovementType.find.byId(fmtId);
		if (financialMovementType == null) {
			return notFound(ControllerHelper.generateJsonResponse(69, Messages.FinanMovType.NOT_FOUND, fmtId));
		}
		
		if (financialMovementType.getUser().getId() != user.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(70, Constants.Messages.FinanMovType.INVALID_FINAN_MOV_TYPE_USER));
		}

		financialMovementType.delete();
		return ok(ControllerHelper.generateJsonResponse(71, Messages.FinanMovType.DELETED));
	}
}
