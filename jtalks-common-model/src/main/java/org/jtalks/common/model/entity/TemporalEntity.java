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
package org.jtalks.common.model.entity;

import org.joda.time.DateTime;

/**
 * This class represents an abstract entity that has creation date and modification date.
 * Date: 09.09.2011<br />
 * Time: 12:48
 *
 * @author Alexey Malev
 */
public abstract class TemporalEntity extends Entity {

    private DateTime creationDate;
    private DateTime modificationDate;

    /**
     * Protected constructor for Hibernate.
     */
    protected TemporalEntity() {

    }

    /**
     * This constructor creates an entity using provided date as creation and modification date.
     *
     * @param creationDate Creation/modification date of an entity.
     */
    public TemporalEntity(DateTime creationDate) {
        this.creationDate = creationDate;
        this.modificationDate = creationDate;
    }

    /**
     * This method updates entity modification date to current date.
     */
    public final void updateModificationDate() {
        this.setModificationDate(new DateTime());
    }

    /**
     * This method returns the date when the entity was created.
     *
     * @return Date when entity was created.
     */
    public DateTime getCreationDate() {
        return creationDate;
    }

    /**
     * <p>This method sets entity creation date to specified value.</p>
     * <p>Method is protected so only Hibernate has access to it.</p>
     *
     * @param creationDate New entity creation date
     */
    protected void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * This method returns entity last modification date.
     *
     * @return Last modification date of the entity.
     */
    public DateTime getModificationDate() {
        return modificationDate;
    }

    /**
     * <p>This method sets entity modification date to specified value.</p>
     *
     * @param modificationDate New entity modification date.
     */
    protected void setModificationDate(DateTime modificationDate) {
        this.modificationDate = modificationDate;
    }
}
