package org.jtalks.common.web.dto;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * <p>This class contains tests for {@link EditUserProfileDto} class.</p>
 * Date: 01.09.2011<br />
 * Time: 17:27
 *
 * @author Alexey Malev
 */
public class EditUserProfileDtoTest {

    @Test
    public void testGetNewPasswordConfirmation() {

        String confirmation = "password confirmation";
        EditUserProfileDto sut = new EditUserProfileDto();
        sut.setNewUserPasswordConfirm(confirmation);

        assertEquals(sut.getNewUserPasswordConfirm(), confirmation);
    }

}
