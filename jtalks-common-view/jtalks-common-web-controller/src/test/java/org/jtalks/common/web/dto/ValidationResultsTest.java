/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.common.web.dto;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * This class contains tests for {@link ValidationResults}
 * Date: 17.08.2011<br />
 * Time: 16:52
 *
 * @author Alexey Malev
 */
public class ValidationResultsTest {

    private BindingResult bindingResult;

    @BeforeMethod
    public void setUp() {
        bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
    }

    @Test
    public void testSuccessfulValidation() {
        when(bindingResult.hasErrors()).thenReturn(false);

        ValidationResults results = new ValidationResults(bindingResult);

        assertTrue(results.isSuccess());
    }

    @Test
    public void testFieldValidationError() {
        String fieldName = "testField";
        String firstErrorMessage = "testErrorMessage1";
        String secondErrorMessage = "testErrorMessage2";
        List<FieldError> errors = new ArrayList<FieldError>();
        errors.add(new FieldError("object", fieldName, firstErrorMessage));
        errors.add(new FieldError("object", fieldName, secondErrorMessage));

        when(bindingResult.getFieldErrors()).thenReturn(errors);

        ValidationResults results = new ValidationResults(bindingResult);

        assertFalse(results.isSuccess());
        assertEquals(results.getFieldErrors().size(), 1);
        assertTrue(results.getFieldErrors().containsKey(fieldName));
        assertEquals(results.getFieldErrors().get(fieldName).size(), 2);
        assertTrue(results.getFieldErrors().get(fieldName).contains(firstErrorMessage));
        assertTrue(results.getFieldErrors().get(fieldName).contains(secondErrorMessage));
        assertEquals(results.getGlobalErrors().size(), 0);
    }

    @Test
    public void testGlobalValidationError() {
        String firstErrorMessage = "testErrorMessage1";
        String secondErrorMessage = "testErrorMessage2";
        List<ObjectError> errors = new ArrayList<ObjectError>();
        errors.add(new ObjectError("object", firstErrorMessage));
        errors.add(new ObjectError("object", secondErrorMessage));

        when(bindingResult.getGlobalErrors()).thenReturn(errors);
        ValidationResults results = new ValidationResults(bindingResult);

        assertFalse(results.isSuccess());
        assertEquals(results.getFieldErrors().size(), 0);
        assertEquals(results.getGlobalErrors().size(), 2);
        assertTrue(results.getGlobalErrors().contains(firstErrorMessage));
        assertTrue(results.getGlobalErrors().contains(secondErrorMessage));
    }

    @Test
    public void testConstructorWithId() {
        long entityId = 1L;

        ValidationResults results = new ValidationResults(bindingResult, entityId);

        assertEquals(results.getEntityId(), entityId);
    }
}
