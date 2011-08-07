package org.jtalks.common.service;

import org.jtalks.common.service.exceptions.NotFoundException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * This class contains unit tests for NotFoundException.
 * Date: 07.08.11<br />
 * Time: 19:58<br />
 *
 * @author Alexey Malev
 */
public class NotFoundExceptionTest {

    @Test
    public void testConstructorWithoutArguments() {
        try {
            throw new NotFoundException();
        } catch (NotFoundException e) {
            assertEquals(null, e.getLocalizedMessage());
        }
    }

    @Test
    public void testConstructorWithMessage() {
        String message = "this is a sample exception message";

        try {
            throw new NotFoundException(message);
        } catch (NotFoundException e) {
            assertEquals(message, e.getLocalizedMessage());
        }
    }
}
