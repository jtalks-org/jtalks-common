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
package org.jtalks.common.util;

import com.googlecode.flyway.core.exception.FlywayException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * This class contains unit tests for {@link FlywayWrapper} class.
 * Date: 22.08.11<br />
 * Time: 0:15<br />
 *
 * @author Alexey Malev
 */
public class FlywayWrapperTest {
    private FlywayWrapper sut;

    @BeforeTest
    public void setUp() {
        sut = new FlywayWrapper();
    }

    /**
     * Exception is thrown here as no real environment available.
     */
    @Test(expectedExceptions = FlywayException.class)
    public void testMigrateWithFlywayEnabled() {
        sut.setEnabled(true);
        sut.migrate();
    }

    @Test
    public void testMigrateWithFlywayDisabled() {
        sut.setEnabled(false);
        assertEquals(sut.migrate(), 0);
    }

}
