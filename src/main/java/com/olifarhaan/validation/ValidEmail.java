package com.olifarhaan.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.validator.routines.EmailValidator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

/**
 * This annotation is used to validate the format of an email address. It
 * considers null as a valid input.
 * 
 * It can be applied to the following elements:
 * <ul>
 * <li>FIELD: This annotation can be used directly on a field to validate its
 * email format.</li>
 * <li>ANNOTATION_TYPE: It can be used as a meta-annotation to create another
 * annotation that validates email format.</li>
 * <li>PARAMETER: This annotation can be used on method parameters to validate
 * the email format of the parameter.</li>
 * </ul>
 */

@Constraint(validatedBy = ValidEmail.CustomEmailValidator.class)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message()

    default "Invalid email format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CustomEmailValidator implements ConstraintValidator<ValidEmail, String> {
        @Override
        public void initialize(ValidEmail constraintAnnotation) {
        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            return email != null && EmailValidator.getInstance().isValid(email);
        }
    }
}
