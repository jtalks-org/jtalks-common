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
import org.jtalks.common.mixin.creation.CreationAwareImpl;
import org.jtalks.common.model.entity.User;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

/**
 * This class contains unit tests for {@link org.jtalks.common.mixin.creation.CreationAwareImpl}
 * Date: 18.09.2011<br />
 * Time: 14:28
 *
 * @author Alexey Malev
 */
public class CreationAwareImplTest {
    private CreationAwareImpl sut;
    private DateTime creationDate;
    private User createdBy;

    @BeforeTest
    public void setUp() {
        creationDate = new DateTime();
        createdBy = mock(User.class);

        sut = new CreationAwareImpl(creationDate, createdBy);
    }

    @Test
    public void testConstructor() {
        assertEquals(sut.getCreatedBy(), createdBy);
        assertEquals(sut.getCreationDate(), creationDate);
    }

    @Test
    public void testSetCreationDate() {
        DateTime newCreationDate = new DateTime(System.currentTimeMillis() + 1000);

        sut.setCreationDate(newCreationDate);

        assertEquals(sut.getCreationDate(), newCreationDate);
    }

    @Test
    public void testSetModifiedBy() {
        User newCreatedBy = mock(User.class);

        sut.setCreatedBy(newCreatedBy);

        assertEquals(sut.getCreatedBy(), newCreatedBy);
    }
}
