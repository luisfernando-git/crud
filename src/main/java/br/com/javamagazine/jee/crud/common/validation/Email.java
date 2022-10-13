package br.com.javamagazine.jee.crud.common.validation;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.*;

@NotNull
@Size(max = 50)
@Constraint(validatedBy = EmailValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

	String message() default "{label.email-nao-valido}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
}