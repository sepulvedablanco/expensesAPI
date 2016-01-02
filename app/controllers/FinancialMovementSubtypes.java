package controllers;

import helpers.ActionAuthenticator;
import helpers.Constants;
import helpers.ControllerHelper;
import helpers.Constants.Messages;
import models.FinancialMovementSubtype;
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
public class FinancialMovementSubtypes extends Controller {

	/**
	 * Action method for GET /user/financialMovementType/<fmtId>/financialMovementSubtypes
	 * 
	 * @param fmtId financial movement type identifier
	 */
	public Result find(Long fmtId) {
		FinancialMovementType fmt = FinancialMovementType.find.byId(fmtId);
		if(fmt == null) {
			return notFound(ControllerHelper.generateJsonResponse(80, Messages.FinanMovType.NOT_FOUND, fmtId));
		}
		
		User user = fmt.getUser();
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(81, Messages.User.TOKEN_ID_CONFLICT));
		}
		
    	String subtypeId = request().getQueryString(Constants.QueryString.ID);
    	Long fmsId = subtypeId == null ? null : Long.parseLong(subtypeId);
    	if(fmsId != null) {
    		return findOne(fmtId, fmsId);
    	}

		if (ControllerHelper.acceptsJson(request())) {
			return ok(Json.toJson(fmt.getFinancialMovementSubtypes()));
		}
		
		if (ControllerHelper.acceptsXml(request())) {
			return ok(views.xml.financialMovementSubtypes.render(fmt.getFinancialMovementSubtypes()));
		}

		return badRequest(ControllerHelper.generateJsonResponse(82, Messages.Common.UNSUPPORTED_FORMAT));
	}
	
	/**
	 * Search for a single financial movement subtype
	 * 
	 * @param fmtId Financial movement type identifier
	 * @param fmsId Financial movement subtype identifier to search
	 *
	 */
	public Result findOne(Long fmtId, Long fmsId) {
		FinancialMovementSubtype financialMovementSubtype = FinancialMovementSubtype.find.byId(fmsId);
		
		if(financialMovementSubtype == null) {
			return notFound(ControllerHelper.generateJsonResponse(83, Messages.FinanMovSubtype.NOT_FOUND, fmsId));
		}
		
		if(financialMovementSubtype.getFinancialMovementType().getId() != fmtId) {
			return badRequest(ControllerHelper.generateJsonResponse(84, Constants.Messages.FinanMovSubtype.INVALID_FINAN_MOV_SUBTYPE_TYPE));
		}
		
		if (ControllerHelper.acceptsJson(request())) {
			return ok(Json.toJson(financialMovementSubtype));
		}

		if (ControllerHelper.acceptsXml(request())) {
			return ok(views.xml._financialMovementSubtype.render(financialMovementSubtype));
		}

		return badRequest(ControllerHelper.generateJsonResponse(85, Messages.Common.UNSUPPORTED_FORMAT));
	}

	
	/**
	 * Action method for POST /user/financialMovementType<fmtId>/financialMovementSubtype.
	 * Financial movement Type attributes must be pass in the body of the request
	 * 
	 * @param fmtId Financial movement type identifier
	 * 
	 */
	public Result create(Long fmtId) {
		FinancialMovementType fmt = FinancialMovementType.find.byId(fmtId);
		if (fmt == null) {
			return notFound(ControllerHelper.generateJsonResponse(86, Messages.FinanMovType.NOT_FOUND, fmtId));
		}
		
		User user = fmt.getUser();
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(87, Messages.User.TOKEN_ID_CONFLICT));
		}

		Form<FinancialMovementSubtype> form = Form.form(FinancialMovementSubtype.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorToJson(88, Constants.Messages.FinanMovSubtype.INVALID, form.errorsAsJson()));
		}

		FinancialMovementSubtype financialMovementSubtype = form.get();
		financialMovementSubtype.setFinancialMovementType(fmt);

		if(financialMovementSubtype.exist(null, fmtId)) {
			return status(Http.Status.CONFLICT, ControllerHelper.generateJsonResponse(89, Messages.FinanMovSubtype.DUPLICATE));
		}
				
		financialMovementSubtype.save();
		return created(ControllerHelper.generateJsonResponse(90, Messages.FinanMovSubtype.CREATED, financialMovementSubtype.getDescription()));
	}
	
	/**
	 * Action method for PUT /user/financialMovementType/<fmtId>/financialMovementSubtype/<fmsId>
	 * Financial movement Type attributes to be modified must be pass in the body of the request
	 * 
	 * @param fmtId Financial movement type identifier
	 * @param fmsId Financial movement subtype identifier to modify
	 */
	public Result update(Long fmtId, Long fmsId) {
		FinancialMovementType fmt = FinancialMovementType.find.byId(fmtId);		
		if (fmt == null) {
			return notFound(ControllerHelper.generateJsonResponse(91, Messages.FinanMovType.NOT_FOUND, fmtId));
		}
		
		User user = fmt.getUser();
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(92, Messages.User.TOKEN_ID_CONFLICT));
		}
						
		Form<FinancialMovementSubtype> form = Form.form(FinancialMovementSubtype.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorToJson(93, Constants.Messages.FinanMovSubtype.INVALID, form.errorsAsJson()));
		}
		
		FinancialMovementSubtype financialMovementSubtype = FinancialMovementSubtype.find.byId(fmsId);
		if(financialMovementSubtype == null) {
			return notFound(ControllerHelper.generateJsonResponse(94, Messages.FinanMovSubtype.NOT_FOUND, fmsId));
		}

		if(financialMovementSubtype.getFinancialMovementType().getId() != fmt.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(95, Constants.Messages.FinanMovSubtype.INVALID_FINAN_MOV_SUBTYPE_TYPE));
		}

		if(financialMovementSubtype.getFinancialMovementType().getUser().getId() != user.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(96, Constants.Messages.FinanMovSubtype.INVALID_FINAN_MOV_SUBTYPE_USER));
		}
		
		FinancialMovementSubtype financialMovementSubtypeMod = form.get();
		if(financialMovementSubtypeMod.exist(financialMovementSubtype.getId(), fmtId)) {
			return status(Http.Status.CONFLICT, ControllerHelper.generateJsonResponse(97, Constants.Messages.FinanMovSubtype.DUPLICATE, financialMovementSubtype.getDescription()));
		}
		
		if (!financialMovementSubtype.changeData(financialMovementSubtypeMod)) {
			return status(Http.Status.NOT_MODIFIED, ControllerHelper.generateJsonResponse(98, Constants.Messages.FinanMovSubtype.NOT_MODIFIED));
		}
		
		financialMovementSubtype.update();
		return ok(ControllerHelper.generateJsonResponse(99, Messages.FinanMovType.UPDATED));
	}
	

	/**
	 * Action method for DELETE /user/financialMovementType/<fmtId>/financialMovementSubtype/<fmsId>
	 * 
	 * @param fmtId Financial movement type identifier to delete
	 * @param fmsId Financial movement subtype identifier to delete
	 */
	public Result delete(Long fmtId, Long fmsId) {
		FinancialMovementType fmt = FinancialMovementType.find.byId(fmtId);		
		if (fmt == null) {
			return notFound(ControllerHelper.generateJsonResponse(100, Messages.FinanMovType.NOT_FOUND, fmtId));
		}
		
		User user = fmt.getUser();
		if(!user.getUser().equals(request().username())) {
			return badRequest(ControllerHelper.generateJsonResponse(101, Messages.User.TOKEN_ID_CONFLICT));
		}
		
		FinancialMovementSubtype financialMovementSubtype = FinancialMovementSubtype.find.byId(fmsId);
		if (financialMovementSubtype == null) {
			return notFound(ControllerHelper.generateJsonResponse(102, Messages.FinanMovSubtype.NOT_FOUND, fmsId));
		}
		
		if (financialMovementSubtype.getFinancialMovementType().getUser().getId() != user.getId()) {
			return badRequest(ControllerHelper.generateJsonResponse(103, Constants.Messages.FinanMovSubtype.INVALID_FINAN_MOV_SUBTYPE_USER));
		}

		financialMovementSubtype.delete();
		return ok(ControllerHelper.generateJsonResponse(104, Messages.FinanMovSubtype.DELETED));
	}
}
