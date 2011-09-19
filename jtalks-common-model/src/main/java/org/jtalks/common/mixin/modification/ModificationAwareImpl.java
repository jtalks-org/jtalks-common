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
package org.jtalks.common.mixin.modification;

import org.joda.time.DateTime;
import org.jtalks.common.model.entity.User;

/**
 * <p>This class is responsible for storing entity last modification date and the last user who modified an entity.</p>
 * Date: 18.09.2011<br />
 * Time: 14:17
 *
 * @author Alexey Malev
 */
public class ModificationAwareImpl implements ModificationAware {

    private DateTime modificationDate;
    private User modifiedBy;

    /**
     * This constructor creates an object with the specified {@code modificationDate} and {@code modifiedBy}
     *
     * @param modificationDate Last modification date of an entity.
     * @param modifiedBy       Last user modified an entity.
     */
    public ModificationAwareImpl(DateTime modificationDate, User modifiedBy) {
        this.modificationDate = modificationDate;
        this.modifiedBy = modifiedBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setModificationDate(DateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setModifiedBy(User user) {
        this.modifiedBy = user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DateTime getModificationDate() {
        return this.modificationDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getModifiedBy() {
        return this.modifiedBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateModification(DateTime modificationDate, User modifiedBy) {
        if (modificationDate != null) {
            if ((this.modificationDate != null) && (modificationDate.compareTo(this.modificationDate) < 0)) {
                throw new IllegalArgumentException("Current modification date [" +
                                                         this.modificationDate +
                                                         "] is after provided modification date [" +
                                                         modificationDate + "].");
            } else {
                this.modificationDate = modificationDate;
            }
        } else {
            if (this.modificationDate != null) {
                throw new IllegalArgumentException("Cannot replace entity modification date [" +
                                                         this.modificationDate + "] with null.");
            }
        }

        if ((this.modifiedBy != null) && (modifiedBy == null)) {
            throw new IllegalArgumentException("Cannot replace last user modified entity: [" + this.modifiedBy +
                                                     "] with null.");
        }

        this.modifiedBy = modifiedBy;
    }
}
