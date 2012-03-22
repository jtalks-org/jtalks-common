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

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * The utilities to work with the object identities of the Spring ACL module.
 *
 * @author stanislav bashkirtsev
 * @see Acls
 * @see Permissions
 */
public interface ObjectIdentities {
    /**
     * Creates {@code ObjectIdentity} for {@code securedObject}.
     *
     * @param securedObject object to create its object identity (which is an ID for the Spring ACL that identifies the
     *                      objects Sids have permissions for)
     * @return identity with {@code securedObject} class name and id
     * @throws IllegalArgumentException if the specified entity doesn't have the id (it's {@code id <= 0})
     */
    ObjectIdentity createIdentityFor(Entity securedObject);

    /**
     * Creates an object identity by its class and id.
     *
     * @param id   the identifier of the domain object
     * @param type the type of the object (often a class name)
     * @return the identity constructed using the supplied identifier and type information.
     */
    ObjectIdentity createIdentity(@Nonnull Serializable id, @Nonnull String type);
}
