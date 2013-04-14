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
package org.jtalks.common.service.transactional;

import org.jtalks.common.model.dao.GenericDao;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.service.exceptions.NotFoundException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.javatalks.utils.datetime.DateTimeUtils;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class AbstractTransactionalEntityServiceTest {
    private class AbstractTransactionalEntityServiceObject extends AbstractTransactionalEntityService {
        private AbstractTransactionalEntityServiceObject(GenericDao dao) {
            this.dao = dao;
        }
    }

    private long ID = 1L;

    private AbstractTransactionalEntityService abstractTransactionalEntityService;
    private GenericDao abstractDao;
    private Entity entity;

    @BeforeMethod
    public void setUp() throws Exception {
        abstractDao = mock(GenericDao.class);
        entity = mock(Entity.class);
        abstractTransactionalEntityService = new AbstractTransactionalEntityServiceObject(abstractDao);
    }


    @Test(expectedExceptions = {NotFoundException.class})
    public void testGetIncorrectId() throws NotFoundException {
        when(abstractDao.isExist(ID)).thenReturn(false);

        abstractTransactionalEntityService.get(ID);
    }

    @Test
    public void testGetCorrectId() throws NotFoundException {
        when(abstractDao.isExist(ID)).thenReturn(true);
        when(abstractDao.get(ID)).thenReturn(entity);

        abstractTransactionalEntityService.get(ID);

        verify(abstractDao).isExist(ID);
        verify(abstractDao).get(ID);
    }

    @Test
    public void testIsExist() {
        when(abstractDao.isExist(ID)).thenReturn(true);

        assertTrue(abstractTransactionalEntityService.isExist(ID));
    }

    @Test
    public void testIsNotExist() {
        when(abstractDao.isExist(ID)).thenReturn(false);

        assertFalse(abstractTransactionalEntityService.isExist(ID));
    }

    @Test
    public void testIfDefaultConstructorSetsSomeDateTimeUtilsImplementation() {
        assertNotNull(abstractTransactionalEntityService.getDateTimeUtils());
    }

    @Test
    public void testSetCustomDateTimeUtils() {
        DateTimeUtils dateTimeUtils = mock(DateTimeUtils.class);

        abstractTransactionalEntityService.setDateTimeUtils(dateTimeUtils);

        assertEquals(abstractTransactionalEntityService.getDateTimeUtils(), dateTimeUtils);
    }
}
