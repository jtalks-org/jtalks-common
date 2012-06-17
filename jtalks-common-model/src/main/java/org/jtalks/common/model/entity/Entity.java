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
package org.jtalks.common.model.entity;

/**
 * Gives uniqueness to objects.
 *
 * @author Pavel Vervenko
 * @author Kirill Afonin
 */
public abstract class Entity {

    private long id;

    private String uuid = java.util.UUID.randomUUID().toString();

    /**
     * Get the primary id of the persistent object.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id for the persistent object.
     *
     * @param id id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the unique id.
     *
     * @return the uuid
     */
    public String getUuid() {
        return this.uuid;
    }

    /**
     * Set the unique id for the persistent object.
     *
     * @param uuid uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Checking if entity has not yet been saved to database.
     *
     * @return true if entity ID does not equal to 0, else - false
     */
    public boolean isPersistent(){
        return getId() != 0;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if ( !(getClass().isInstance(obj)) ) {
            return false;
        }
        Entity other = (Entity) obj;
        return getUuid().equals(other.getUuid());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
