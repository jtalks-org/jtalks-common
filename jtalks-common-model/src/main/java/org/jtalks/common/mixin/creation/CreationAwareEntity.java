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
 * This interface contains methods that implementing class should have
 * to be aware of it's creation date and of user who created an entity.
 * Date: 13.09.2011<br />
 * Time: 15:41
 *
 * @author Alexey Malev
 */
public interface CreationAwareEntity {

    /**
     * This method returns the date when the object was created.
     *
     * @return Date when the object was created.
     */
    DateTime getCreationDate();

    /**
     * This method returns the user created the object.
     *
     * @return User created the object.
     */
    User getCreatedBy();
}
