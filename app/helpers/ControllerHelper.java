package helpers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.http.entity.ContentType;

import helpers.Constants.JsonResponseTag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Http.Request;

public class ControllerHelper {

	public static JsonNode generateJsonResponse(Integer code, String message, Object... messageParams) {
		ObjectNode node = Json.newObject();
		node.put(JsonResponseTag.CODE, code);
		node.put(JsonResponseTag.MESSAGE, Messages.get(message, messageParams));
		return node;
	}
	
	public static JsonNode errorToJson(Integer code, String message, JsonNode errors) {
		ObjectNode node = Json.newObject();
		node.put(JsonResponseTag.CODE, code);
		node.put(JsonResponseTag.MESSAGE, Messages.get(message));
		node.putPOJO(JsonResponseTag.ERRORS, errors);
		return node;
	}
	
	public static boolean acceptsJson(Request request) {
		return request.accepts(ContentType.APPLICATION_JSON.getMimeType());
	}
	
	public static boolean acceptsXml(Request request) {
		return (request.accepts(ContentType.APPLICATION_XML.getMimeType()) || 
				request.accepts(ContentType.TEXT_XML.getMimeType()));
	}
	
	public static String format(Request request, BigDecimal number) {
		Locale locale = Locale.getDefault();
		if(!request.acceptLanguages().isEmpty()) {
			locale = new Locale(request.acceptLanguages().get(0).code());
		}
		
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		return df.format(number);
	}

}
