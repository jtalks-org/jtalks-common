/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
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
            "+[a-zA-Z0-9])|([a-zA-Z0-9]{1,2}))[\\.]{1})+([a-zA-Z]{2,})))$";
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
