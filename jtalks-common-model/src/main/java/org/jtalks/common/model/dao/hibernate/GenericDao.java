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
import org.jtalks.common.model.dao.Crud;
import org.jtalks.common.model.entity.Entity;


/**
 * Basic class for access to the specified {@link Entity} objects.
 * Uses to load objects from database, save, update or delete them.
 * The implementation is based on the Hibernate.
 *
 * @author Alexandre Teterin
 */
public class GenericDao<T extends Entity> implements Crud<T> {

    /**
     * Hibernate SessionFactory
     */
    private final SessionFactory sessionFactory;

    /**
     * Type of entity
     */
    private final Class<T> type;

    /**
     * @param sessionFactory The SessionFactory.
     */
    public GenericDao(SessionFactory sessionFactory, Class<T> type) {
        this.sessionFactory = sessionFactory;
        this.type = type;
    }

    /**
     * Get current Hibernate session.
     *
     * @return current Session
     */
    public Session session() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * Save or update entity.
     * <p/>
     * This operation cascades to associated instances if the association
     * is mapped with cascade="save-update".
     * This operation will result in INSERT SQL statement (for saving new entity),
     * or in UPDATE statement for updating existing entity.
     *
     * @param entity object to save
     */
    @Override
    public void saveOrUpdate(T entity) {
        session().saveOrUpdate(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) {
        String deleteQuery = "delete " + type.getCanonicalName() + " e where e.id= :id";
        return session().createQuery(deleteQuery).setLong("id", id).executeUpdate() != 0;
    }

    /**
     * <p>Delete the entity by object reference.</p>
     * <p>This method deletes all cascaded references.</p>
     *
     * @param entity Entity to be deleted.
     * @throws {@link org.hibernate.StaleStateException} Throws exception if entity not exist.
     */
    @Override
    public void delete(T entity) {
        session().delete(entity);
    }

    /**
     * Return the persistent instance of the parametrised entity class with the given identifier,
     * or null if there is no such persistent instance. (If the instance is already associated with the session,
     * return that instance. This method never returns an uninitialized instance.)
     * @param id The entity id.
     * @return A persistent instance or null.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T get(Long id) {
        return (T) session().get(type, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExist(Long id) {
        return get(id) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        session().flush();
    }
}
