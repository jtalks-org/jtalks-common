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
