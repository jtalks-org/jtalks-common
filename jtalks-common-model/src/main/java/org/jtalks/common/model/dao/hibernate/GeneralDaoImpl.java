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

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.jtalks.common.model.dao.GeneralDao;
import org.jtalks.common.model.entity.Entity;

/**
 * Basic class for access to the {@link Entity} objects.
 * Uses to load objects from database, save, update or delete them.
 * The implementation is based on the Hibernate.
 * Has the implementation of some commonly used methods.
 *
 * @author Pavel Vervenko
 * @author Kirill Afonin
 */
public abstract class GeneralDaoImpl implements GeneralDao {

    /**
     * Hibernate SessionFactory
     */
    private SessionFactory sessionFactory;


    /**
     * Get current Hibernate session.
     *
     * @return current Session
     */
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Setter for Hibernate SessionFactory.
     *
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void update(T entity) {
        Session session = getSession();
        session.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void saveOrUpdate(T entity) {
        Session session = getSession();
        session.saveOrUpdate(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean delete(Class<T> type, Long id) {
        String deleteQuery = "delete " + type.getCanonicalName()
                + " e where e.id= :id";
        return getSession().createQuery(deleteQuery).setCacheable(true).setLong("id", id).executeUpdate() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void delete(T entity) {
        getSession().delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type, Long id) {
        return (T) getSession().get(type, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean isExist(Class<T> type, Long id) {
        return get(type, id) != null;
    }
}
