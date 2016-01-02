package helpers;

/**
 * 
 * @author Diego Sepulveda Blanco
 *
 */
public class Constants {

	public static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";
	
	public class Headers {
		
		public static final String USER_TOKEN = "X-AUTH-TOKEN";
	}
	
	public class QueryString {

		public static final String ID = "id";

		public static final String CONCEPT = "c";
		public static final String CONCEPT_LIKE = "cl";
		public static final String EXPENSE = "e";
		public static final String TYPE = "t";
		public static final String TYPE_DESCRIPTION = "td";
		public static final String SUBTYPE = "s";
		public static final String SUBTYPE_DESCRIPTION = "sd";
		public static final String BANK_ACCOUNT = "b";
		public static final String BANK_ACCOUNT_DESCRIPTION = "bd";
		public static final String YEAR = "y";
		public static final String MONTH = "m";
		public static final String DAY = "d";
		public static final String LIMIT = "l";
		public static final String OFFSET = "o";
		
		public static final int DEFAULT_PAGE_SIZE = 10;
		
	}
	
	public class Messages {
		
		public class Common {
			
			public static final String UNSUPPORTED_FORMAT = "common.unsupported_format";
			public static final String PAGE = "common.page";
			public static final String PAGE_TOTAL_MOVEMENTS = "common.page_total_movements";			
			
		}
		
		public class User {
			
			public static final String INVALID = "user.invalid";
			public static final String INVALID_PASSWORD = "user.invalid_password";			
			public static final String DUPLICATE_USER_NAME = "user.duplicate_user";
			public static final String CREATED = "user.created";
			public static final String UPDATED = "user.updated";	
			public static final String DELETED = "user.deleted";
			public static final String NOT_MODIFIED = "user.not_modified";
			public static final String NOT_FOUND = "user.not_found";
			public static final String TOKEN_ID_CONFLICT = "user.token_id_conflict";
			public static final String NUMBER_OF_PARAMETERS = "user.incorrect_number_of_parameters";
			public static final String LOGIN_FAILURE = "user.login_failure";

		}
		
		public class BankAccount {
			
			public static final String INVALID_BANK_ACCOUNT_USER = "bankAccount.invalid_bank_account_user";
			public static final String INVALID = "bankAccount.invalid";
			public static final String DUPLICATE = "bankAccount.duplicate";
			public static final String CREATED = "bankAccount.created";
			public static final String UPDATED = "bankAccount.updated";	
			public static final String DELETED = "bankAccount.deleted";
			public static final String NOT_MODIFIED = "bankAccount.not_modified";
			public static final String NOT_FOUND = "bankAccount.not_found";

		}
		
		public class FinanMovType {

			public static final String INVALID_FINAN_MOV_TYPE_USER = "finanMovType.invalid_finan_mov_type_user";
			public static final String INVALID = "finanMovType.invalid";
			public static final String DUPLICATE = "finanMovType.duplicate";
			public static final String CREATED = "finanMovType.created";
			public static final String UPDATED = "finanMovType.updated";
			public static final String DELETED = "finanMovType.deleted";
			public static final String NOT_MODIFIED = "finanMovType.not_modified";
			public static final String NOT_FOUND = "finanMovType.not_found";
			
		}
		
		public class FinanMovSubtype {

			public static final String INVALID_FINAN_MOV_SUBTYPE_USER = "finanMovSubtype.invalid_finan_mov_subtype_user";
			public static final String INVALID_FINAN_MOV_SUBTYPE_TYPE = "finanMovSubtype.invalid_finan_mov_subtype_type";
			public static final String INVALID = "finanMovSubtype.invalid";
			public static final String DUPLICATE = "finanMovSubtype.duplicate";
			public static final String CREATED = "finanMovSubtype.created";
			public static final String UPDATED = "finanMovSubtype.updated";
			public static final String DELETED = "finanMovSubtype.deleted";
			public static final String NOT_MODIFIED = "finanMovSubtype.not_modified";
			public static final String NOT_FOUND = "finanMovSubtype.not_found";
			
		}
		
		public class FinanMov {

			public static final String INVALID_FINAN_MOV_USER = "finanMov.invalid_finan_mov_user";
			public static final String INVALID = "finanMov.invalid";
			public static final String CREATED = "finanMov.created";
			public static final String UPDATED = "finanMov.updated";
			public static final String DELETED = "finanMov.deleted";
			public static final String NOT_FOUND = "finanMov.not_found";
			
		}

	}
	
	public class JsonResponseTag {
		
		public static final String CODE = "code";
		public static final String MESSAGE = "message";
		public static final String ERRORS = "errors";

	}
	
	public class CustomTableNames {
		
		public static final String USER = "consumer";
		
	}
	
	public class CustomColumnNames {
		
		public static final String USER_USERNAME = "username";
		
	}
	
	public class Sequences {

		public static final String SEQ_SUFFIX = "_seq";
		public static final String SUFFIX = "_gen";

		public static final String USER = "consumer" + SEQ_SUFFIX;
		public static final String USER_GEN = USER + SUFFIX;
		public static final String BANK_ACCOUNT = "bank_account" + SEQ_SUFFIX;
		public static final String BANK_ACCOUNT_GEN = BANK_ACCOUNT + SUFFIX;
		public static final String FINAN_MOV = "financial_movement" + SEQ_SUFFIX;
		public static final String FINAN_MOV_GEN = FINAN_MOV + SUFFIX;
		public static final String FINAN_MOV_TYPE = "financial_movement_type" + SEQ_SUFFIX;
		public static final String FINAN_MOV_TYPE_GEN = FINAN_MOV_TYPE + SUFFIX;
		public static final String FINAN_MOV_SUBTYPE = "financial_movement_subtype" + SEQ_SUFFIX;
		public static final String FINAN_MOV_SUBTYPE_GEN = FINAN_MOV_SUBTYPE + SUFFIX;

	}

}
