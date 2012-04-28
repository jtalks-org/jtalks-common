package org.jtalks.common.validation.email;

import org.jtalks.common.validation.annotations.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Email validator.
 * <p>
 * Check if mail corresponds the reqular expression template, based on
 * <a href="http://www.faqs.org/rfcs/rfc2822.html">RFC 2822</a>
 * </p>
 * 
 * @author Ancient_Mariner
 */

public class EmailValidator implements ConstraintValidator<Email, String>{

    private static final String regEx = "^[a-zA-Z0-9_'+*/^&=?~{}\\-](\\.?[a-zA-Z0-9_'+*/^&=?~{}\\-])" +      //atom
            "*\\@((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(\\:\\d{1,3})?)|(((([a-zA-Z0-9][a-zA-Z0-9\\-]" + //domain
            "+[a-zA-Z0-9])|([a-zA-Z0-9]{1,2}))[\\.]{1})+([a-zA-Z]{2,6})))$";
    Pattern pattern;

    /**
     *@inheritDoc
     */
    @Override
    public void initialize(Email constraintAnnotation) {
        pattern = Pattern.compile(regEx);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext){
        Matcher matcher = pattern.matcher(object);

        return matcher.matches();
    }
}
