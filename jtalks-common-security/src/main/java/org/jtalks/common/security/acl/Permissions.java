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
package org.jtalks.common.security.acl;

import org.jtalks.common.model.entity.Entity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import java.util.List;

/**
 * The utility to work with the {@link Permission}. Even though it touches {@link org.springframework.security.acls.model.Acl},
 * this is only for purpose of granting/removing/restricting permissions.
 *
 * @author stanislav bashkirtsev
 * @see ObjectIdentities
 * @see Acls
 */
public interface Permissions {
    /**
     * Apply every permission from list to every sid from list.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      securable object
     * @return the ACL that serves the {@code sids}
     */
    ExtendedMutableAcl grant(List<? extends Sid> sids, List<Permission> permissions, Entity target);

    /**
     * Apply every permission from list to every sid from list.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      securable object
     * @return the mutable acl that was created/retrieved while the operation
     */
    ExtendedMutableAcl restrict(List<? extends Sid> sids, List<Permission> permissions, Entity target);

    ExtendedMutableAcl delete(List<? extends Sid> sids, List<Permission> permissions, Entity target);

    /**
     * Delete permissions from {@code acl} for every sid.
     *
     * @param acl         the acl to remove the sid permissions from it
     * @param sids        list of sids to remove their permissions
     * @param permissions list of permissions to remove them
     */
    void deletePermissionsFromAcl(
            ExtendedMutableAcl acl, List<? extends Sid> sids, List<Permission> permissions);
}
