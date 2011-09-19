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
package org.jtalks.common.mixin.creation;

import org.joda.time.DateTime;
import org.jtalks.common.model.entity.User;

/**
 * <p>This class is responsible for storing entity creation date and user who created an entity.</p>
 * Date: 13.09.2011<br />
 * Time: 16:05
 *
 * @author Alexey Malev
 */
public class CreationAwareImpl implements CreationAware {
    private DateTime creationDate;
    private User createdBy;

    /**
     * This constructor creates {@link CreationAwareImpl} with the specified date and user.
     *
     * @param creationDate Date set as object creation date.
     * @param createdBy    User set as object creator.
     */
    public CreationAwareImpl(DateTime creationDate, User createdBy) {
        this.creationDate = creationDate;
        this.createdBy = createdBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DateTime getCreationDate() {
        return this.creationDate;
    }

    /**
     * This method sets date when this entity was created.
     *
     * @param creationDate Date to set as creation date.
     */
    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getCreatedBy() {
        return this.createdBy;
    }

    /**
     * This method sets the user created then entity.
     *
     * @param createdBy User to set as entity creator.
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
