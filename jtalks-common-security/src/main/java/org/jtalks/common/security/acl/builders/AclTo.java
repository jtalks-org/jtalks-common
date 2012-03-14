package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Entity;

import javax.annotation.Nonnull;

/**
 * A step while creating/modifying ACL structure to assign permissions to some sid entity. Sid is an object that can
 * undertake some actions, like user or group of users.
 *
 * @author stanislav bashkirtsev
 * @see AclAction
 * @see AclFrom
 * @see AclOn
 * @see AclFlush
 * @since 0.13
 */
public interface AclTo<T extends Entity> {
    /**
     * Assigns (or restrict) the permission to the specified SIDs.
     *
     * @param sids the objects (users, groups of users) to get permissions to undertake some action on object identity
     * @return the next action to be performed - choosing the object identity to assign permissions for
     */
    AclOn to(@Nonnull T... sids);
}
