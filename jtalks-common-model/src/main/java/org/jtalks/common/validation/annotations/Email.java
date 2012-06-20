package org.jtalks.common.validation.annotations;

import org.jtalks.common.validation.email.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

 /**
  * Annotation for checking mail, which corresponds the <a href="http://www.faqs.org/rfcs/rfc2822.html">RFC 2822</a>.
  * <p> Was created instead of usage of Hibernate @Email annotation due to need of custom check for Email format.
  * For example for checking of mail with + sign in the middle of address, such as <b>mail+1@mail.com</b> </p>
  *
  * The standard {@link org.hibernate.validation.constraints.Email} did not meet our requirements
  *
  * @author Ancient_Mariner
  * @see EmailValidator
 */

@Target( {FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
@ReportAsSingleViolation
@Pattern(regexp = "^[a-zA-Z0-9_'+*/^&=?~{}\\-](\\.?[a-zA-Z0-9_'+*/^&=?~{}\\-])" +
        "*\\@((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(\\:\\d{1,3})?)|(((([a-zA-Z0-9][a-zA-Z0-9\\-]" +
        "+[a-zA-Z0-9])|([a-zA-Z0-9]{1,2}))[\\.]{1})+([a-zA-Z]{2,6})))$"
       )

public @interface Email {

    String message() default "{user.email.email_format_constraint_violation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
