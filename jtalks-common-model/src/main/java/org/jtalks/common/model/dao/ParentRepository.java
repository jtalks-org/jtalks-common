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
package org.jtalks.common.model.dao;

import org.jtalks.common.model.entity.Entity;

/**
 * @author Kirill Afonin
 */
public interface ParentRepository<T extends Entity> extends ChildRepository<T> {

    /**
     * Save or update entity.
     *
     * @param entity object to save
     */
    void saveOrUpdate(T entity);

    /**
     * <p>Delete the entity by id.</p>
     * <b>Please note - this method doesn't delete cascaded entities.</b>
     *
     * @param id the id
     * @return {@code true} if entity deleted successfully
     */
    boolean delete(Long id);

    /**
     * <p>Delete the entity by object reference.</p>
     * <p>This method deletes all cascaded references.</p>
     *
     * @param entity Entity to be deleted.
     */
    void delete(T entity);
}
