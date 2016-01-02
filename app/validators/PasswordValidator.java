package validators;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;
import play.data.validation.Constraints;
import play.libs.F.Tuple;

public class PasswordValidator extends Constraints.Validator<String> 
	implements ConstraintValidator<Annotation, String> {

	private static final String PASSWORD_PATTERN = 
			"((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
	
	@Override
	public boolean isValid(String password) {
		if(password == null) {
			return false;
		}
		return password.matches(PASSWORD_PATTERN);
	}

	@Override
	public void initialize(Annotation arg0) {		
	}
	
	@Override
	public Tuple<String, Object[]> getErrorMessageKey() {
		return null;
	}

}
