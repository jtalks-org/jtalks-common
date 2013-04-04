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
package org.jtalks.common.model.dao;

import org.jtalks.common.model.entity.Entity;

/**
 * Describes a DAO for typical child-type entity. Such an entity may be updated
 * on it's own, but for deletion it should use the following pattern: remove
 * an entity from parent's collection, save parent afterwards.
 * To prevent misuse a dao interface for child repository lacks "delete(...)"
 * methods; these methods are eligible for parent-class entities and 
 * specified by ParentRepositoryDao interface.
 *
 * Example: Topic has a collection of posts. Post is a child entity, 
 * Topic is a parent one. So, PostDao should implement ChildRepository.
 *
 * @author Pavel Vervenko
 * @author Kirill Afonin
 * @author Alexey Malev
 */
public interface ChildRepository<T extends Entity> {

    /**
     * Save or update entity.
     *
     * @param entity object to save
     */
    void saveOrUpdate(T entity);

    /**
     * Get entity by id.
     *
     * @param id the id
     * @return loaded Persistence instance
     */
    T get(Long id);

    /**
     * Check entity existence by id.
     *
     * @param id entity id
     * @return {@code true} if entity exist
     */
    boolean isExist(Long id);
}
