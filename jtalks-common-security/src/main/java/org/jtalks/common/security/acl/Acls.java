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
import org.springframework.security.acls.model.ObjectIdentity;

/**
 * The utilities to work with Spring Security {@link org.springframework.security.acls.model.Acl} classes. It doesn't
 * work with permissions directly.
 *
 * @author stanislav bashkirtsev
 * @see ObjectIdentities
 * @see Permissions
 */
public interface Acls {
    /**
     * Get existing ACL record for the entity. If ACL does not exist it will be created.
     *
     * @param entity entity to get is {@link org.springframework.security.acls.model.ObjectIdentity} which is an ACL id
     *               of the entry and find/create its ACL object
     * @return Access Control List for the specified entity
     */
    ExtendedMutableAcl getAclFor(Entity entity);

    /**
     * Get existing ACL for identity. If ACL does not exist it will be created.
     *
     * @param oid object identity to get its ACL
     * @return ACL or the specified object identity
     */
    ExtendedMutableAcl getAclFor(ObjectIdentity oid);
}
