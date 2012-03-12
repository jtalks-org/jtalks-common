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
