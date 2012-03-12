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
