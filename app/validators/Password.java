package validators;

import helpers.Constants;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
	
	String message() default Constants.Messages.User.INVALID_PASSWORD;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}