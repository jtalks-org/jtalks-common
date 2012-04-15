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
package org.jtalks.common.mixin;

import org.joda.time.DateTime;
import org.jtalks.common.mixin.modification.ModificationAwareImpl;
import org.jtalks.common.model.entity.User;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * This class contains unit tests for {@link org.jtalks.common.mixin.modification.ModificationAwareImpl}
 * Date: 18.09.2011<br />
 * Time: 14:28
 *
 * @author Alexey Malev
 */
public class ModificationAwareImplTest {
    private ModificationAwareImpl sut;
    private DateTime modificationDate;
    private User modifiedBy;

    @BeforeTest
    public void setUp() {
        modificationDate = new DateTime();
        modifiedBy = mock(User.class);

        sut = new ModificationAwareImpl(modificationDate, modifiedBy);
    }

    @Test
    public void testConstructor() {
        assertEquals(sut.getModifiedBy(), modifiedBy);
        assertEquals(sut.getModificationDate(), modificationDate);
    }

    @Test
    public void testSetModificationDate() {
        DateTime newCreationDate = new DateTime(System.currentTimeMillis() + 1000);

        sut.setModificationDate(newCreationDate);

        assertEquals(sut.getModificationDate(), newCreationDate);
    }

    @Test
    public void testSetModifiedBy() {
        User newCreatedBy = mock(User.class);

        sut.setModifiedBy(newCreatedBy);

        assertEquals(sut.getModifiedBy(), newCreatedBy);
    }

    @Test
    public void testUpdateExistingModificationDate() {
        DateTime newModificationDate = new DateTime(sut.getModificationDate().getMillis() + 100);

        sut.updateModification(newModificationDate, modifiedBy);

        assertEquals(sut.getModificationDate(), newModificationDate);
    }

    @Test
    public void testUpdateNullModificationDate() {
        DateTime newModificationDate = new DateTime();

        ModificationAwareImpl sut = new ModificationAwareImpl(null, null);

        sut.updateModification(newModificationDate, modifiedBy);

        assertEquals(sut.getModificationDate(), newModificationDate);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateModificationDateToThePast() {
        DateTime newModificationDate = new DateTime(sut.getModificationDate().getMillis() - 100);

        sut.updateModification(newModificationDate, modifiedBy);
    }

    @Test
    public void testUpdateModificationDateToEqualDate() {
        DateTime currentModificationDate = sut.getModificationDate();

        sut.updateModification(currentModificationDate, modifiedBy);

        assertEquals(sut.getModificationDate(), currentModificationDate);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateExistingModificationDateToNull() {
        sut.updateModification(null, modifiedBy);
    }

    @Test
    public void testUpdateNullModificationDateToNull() {
        ModificationAwareImpl sut = new ModificationAwareImpl(null, modifiedBy);

        sut.updateModification(null, modifiedBy);

        assertNull(sut.getModificationDate());
    }

    @Test
    public void testUpdateNullModifiedBy() {
        ModificationAwareImpl sut = new ModificationAwareImpl(null, null);

        sut.updateModification(null, modifiedBy);

        assertEquals(sut.getModifiedBy(), modifiedBy);
    }

    @Test
    public void testUpdateNullModifiedByToNull() {
        ModificationAwareImpl sut = new ModificationAwareImpl(null, null);

        sut.updateModification(modificationDate, null);

        assertNull(sut.getModifiedBy());
    }

    @Test
    public void testUpdateModifiedBy() {
        User newModifiedBy = mock(User.class);

        sut.updateModification(sut.getModificationDate(), newModifiedBy);

        assertEquals(sut.getModifiedBy(), newModifiedBy);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateModifiedByToNull() {
        sut.updateModification(sut.getModificationDate(), null);
    }
}
