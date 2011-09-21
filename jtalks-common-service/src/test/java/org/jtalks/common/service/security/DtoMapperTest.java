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
package org.jtalks.common.service.security;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * This class contains unit tests for {@link DtoMapper}
 * Date: 16.09.2011<br />
 * Time: 18:11
 *
 * @author Alexey Malev
 */
public class DtoMapperTest {

    private DtoMapper sut;

    @BeforeMethod
    public void setUp() {
        sut = new DtoMapper();
    }

    @Test
    public void testGetMappingWithAnnotation() {
        assertEquals(sut.getMapping(TestDtoWithAnnotation.class.getCanonicalName()), String.class);
    }

    @Test
    public void testGetMappingWithoutAnnotation() {
        assertNull(sut.getMapping(TestDtoWithoutAnnotation.class.getCanonicalName()));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testGetMappingForNonExistentClass() {
        sut.getMapping("ya.class");
    }
}
