package org.jtalks.common.validation.annotations;

import org.jtalks.common.validation.email.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

 /**
 * Annotation for checking mail, which corresponds the RFC2822.
 *
 * @author Ancient_Mariner
 * @see EmailValidator
 */

@Target( {FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface Email {

    String message() default "{org.jtalks.common.validation.email.valid_Email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
