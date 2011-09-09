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
package org.jtalks.common.model.entity;

import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * This class contains unit tests for {@link TemporalEntity}
 * Date: 09.09.2011<br />
 * Time: 13:00
 *
 * @author Alexey Malev
 */
public class TemporalEntityTest {

    private class SomeEntity extends TemporalEntity {
        public SomeEntity() {
            super(new DateTime());
        }
    }

    private SomeEntity sut;

    @BeforeMethod
    public void setUp() {
        sut = new SomeEntity();
    }

    @Test
    public void testConstructorSetsDates() {
        assertNotNull(sut.getCreationDate());
        assertNotNull(sut.getModificationDate());
    }

    @Test
    public void testConstructorSetsEqualDates() {
        assertEquals(sut.getCreationDate(), sut.getModificationDate());
    }

    @Test
    public void testSetCreationDate() {
        DateTime currentCreationDate = sut.getCreationDate();
        sut.setCreationDate(new DateTime(System.currentTimeMillis() + 100000));

        assertNotSame(sut.getCreationDate(), currentCreationDate);
    }

    @Test
    public void testSetModificationDate() {
        DateTime currentModificationDate = sut.getModificationDate();
        sut.setModificationDate(new DateTime(System.currentTimeMillis() + 100000));

        assertNotSame(sut.getModificationDate(), currentModificationDate);
    }

    @Test
    public void testUpdateModificationDate() {
        try {
            DateTime currentModificationDate = sut.getModificationDate();
            Thread.sleep(50);
            sut.updateModificationDate();
            assertNotSame(sut.getModificationDate(), currentModificationDate);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("InterruptedException shouldn't be thrown here.", e);
        }
    }
}
