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

import org.jtalks.common.model.dao.ChildRepository;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.service.EntityService;
import org.jtalks.common.service.exceptions.NotFoundException;
import ru.javatalks.utils.datetime.DateTimeUtils;
import ru.javatalks.utils.datetime.DateTimeUtilsFactory;


/**
 * Generic implementation of all entity based services.
 * Most of the implementations of the methods are basing on straightforward calls
 * of the same named method from DAO interface.
 *
 * @author Osadchuck Eugeny
 * @author Kirill Afonin
 */
public abstract class AbstractTransactionalEntityService<T extends Entity, Y extends ChildRepository<T>>
    implements EntityService<T> {
    /**
     * Dao object implementation.
     */
    protected Y dao;

    private DateTimeUtils dateTimeUtils;

    /**
     * This constructor creates abstract transactional entity service with the default implementation of {@link
     * DateTimeUtils} returned by {@link DateTimeUtilsFactory#getDateTimeUtils()}
     */
    public AbstractTransactionalEntityService() {
        this.dateTimeUtils = DateTimeUtilsFactory.getDateTimeUtils();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(Long id) throws NotFoundException {
        if (!dao.isExist(id)) {
            throw new NotFoundException("Entity with id: " + id + " not found");
        }
        return dao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExist(long id) {
        return dao.isExist(id);
    }

    /**
     * This method returns {@link DateTimeUtils} used by this service.
     *
     * @return {@link DateTimeUtils} used by this service.
     */
    public DateTimeUtils getDateTimeUtils() {
        return dateTimeUtils;
    }

    /**
     * This method sets {@link DateTimeUtils} that is going to be used by this service. Protected,
     * as it should be set only for test purposes.
     *
     * @param dateTimeUtils New {@link DateTimeUtils} going to be used by this service.
     */
    protected void setDateTimeUtils(DateTimeUtils dateTimeUtils) {
        this.dateTimeUtils = dateTimeUtils;
    }
}