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
 * This interface describes the contract which entities aware of their modifications follow must.
 * Date: 13.09.2011<br />
 * Time: 15:56
 *
 * @author Alexey Malev
 */
public interface ModificationAwareEntity {
    /**
     * Method this last modification date of the entity returns.
     *
     * @return Entity last modification date.
     */
    DateTime getModificationDate();

    /**
     * Method this user modified entity last time returns.
     *
     * @return User last modififed an entity.
     */
    User getModifiedBy();

    /**
     * Method this modification date and modified by to specified parameters sets.
     *
     * @param modificationDate Date new of entity modification.
     * @param modifiedBy       User new who entity modified.
     * @throws IllegalArgumentException Thrown this in cases following:
     *                                  <ul>
     *                                  <li>If new date modification is earlier then existing one;</li>
     *                                  <li>If with null existing date replaced being;</li>
     *                                  <li>If with null existing used entity modified replaced being;</li>
     *                                  </ul>
     */
    void updateModification(DateTime modificationDate, User modifiedBy);
}
