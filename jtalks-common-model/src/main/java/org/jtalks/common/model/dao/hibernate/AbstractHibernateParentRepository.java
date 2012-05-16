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
package org.jtalks.common.model.dao.hibernate;

import org.hibernate.classic.Session;
import org.jtalks.common.model.dao.ParentRepository;
import org.jtalks.common.model.entity.Entity;

/**
 * This class provides methods to create and delete entity
 * in addition to methods provided by {@link AbstractHibernateChildRepository}
 *
 * @author Kirill Afonin
 * @author Alexey Malev
 */
public abstract class AbstractHibernateParentRepository<T extends Entity> extends AbstractHibernateChildRepository<T>
    implements ParentRepository<T> {

    private final String deleteQuery = "delete " + getType().getCanonicalName() + " e where e.id= :id";

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdate(T entity) {
        Session session = getSession();
        session.saveOrUpdate(entity);
        session.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) {
        return getSession().createQuery(deleteQuery).setCacheable(true).setLong("id", id).executeUpdate() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }
}
