package br.com.javamagazine.jee6.crud.common.validation;

import java.util.regex.*;
import javax.validation.*;

public class EmailValidator implements ConstraintValidator<Email, String> {
	
	private Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
	
	@Override
	public void initialize(Email email) {}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Matcher m = pattern.matcher(value);
		return m.matches();
	}
}