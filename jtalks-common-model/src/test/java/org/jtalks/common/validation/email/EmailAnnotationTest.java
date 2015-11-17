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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Checks that a given string is a well-formed email address.
 * <p>
 * The specification of a valid email can be found in
 * <a href="http://www.faqs.org/rfcs/rfc2822.html">RFC 2822</a>
 * and one can come up with a regular expression matching 
 * <a href="http://www.ex-parrot.com/~pdw/Mail-RFC822-Address.html">
 * all valid email addresses</a> . This implementation is a custom implementation written on a base of annotations.
 * It gives two arrays of valid/invalid emails and checks whether they are valid/invalid to use.
 * </p>
 *
 * @author Ancient_Mariner
 */

public class EmailAnnotationTest {

    MailEntity mailEntity;
    private Validator validator;

    @BeforeMethod
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mailEntity = new MailEntity();
    }

    @Test(dataProvider = "validEmails")
    public void testValidEmails(String[] validEmails) throws Exception {
        int expectedErrorsQty = 0;

        for(String validMailSelector : validEmails){
            mailEntity.setMail(validMailSelector);

            Set<ConstraintViolation<MailEntity>> constraintViolation = validator.validate(mailEntity);
            assertEquals(expectedErrorsQty, constraintViolation.size());
        }
    }

    @Test(dataProvider = "invalidEmails", dependsOnMethods = "testValidEmails")
    public void testInValidEmails(String[] invalidEmails) throws Exception{
        int expectedErrorQty = 1;

        for(String invalidMailSelector : invalidEmails){
            mailEntity.setMail(invalidMailSelector);

            Set<ConstraintViolation<MailEntity>> constraintViolation = validator.validate(mailEntity);
            assertEquals(expectedErrorQty, constraintViolation.size());
        }
    }

    @DataProvider
    public Object[][] validEmails(){
        return new Object[][]{{new String[]{
                "aLeXaNdeR@gmail.com",
                "a_l_3+}{*N-D~3RR@g3M4i5l.us.oRg",
                "Dmi't/^ri&y@MaIl.nEt.uK",
                "ST+N'~/+777@Yah00.org.ru",
                "seaman_sells_sea_shells@onTheSea.shore",
                "MaIl+mAiL*MaiL+25=&-@twit.oRg.Br",
                "~DArK_L0Rd_of_D3strUctI0N~@noNeed.of.mail.box",
                "Frue_in_der_Frische_fischer@Fritz.fischt.frische.Fische",
                "less.luter@email.PHOTOGRAPHY",
                "itIsNormalEmail@withLong.DomainName",
                "itIsNormalEmail@withLong.DomainNameVeeeeeryyyyyLooongDoooomaaaainNaaameee"
        }}};
    }

    @DataProvider
    public Object[][] invalidEmails(){
        return new Object[][]{{new String[]{
                "inv@lidm@il,zuzu",
                "3^;;^0mail@.co.net.our.domen.does.not.work",
                "mambA#five.song.good.one",
                ")(@|\\|o|%.com.uta",
                "$pirit of the w@ashington m@il",
                "dUd3, there d@ f*cK my m@il is!???",
                "**@Nd I h3@Rd @$ it w3R3 ThE n0is3_of_thunder\n" +
                  "One of the four beasts saying come and see and I saw\n" +
                  "And behold a white horse**",
                ")-0-(It's Alpha and Omega's kingdom come)^-^(",
                "\\\\\\Listen_to_the_words_long_written_down@When.the.Man.comes.around//////"

        }}};
    }

    public class MailEntity {

        @Email
        private String email;

        public void setMail(String email){
           this.email = email;
        }
    }



}
