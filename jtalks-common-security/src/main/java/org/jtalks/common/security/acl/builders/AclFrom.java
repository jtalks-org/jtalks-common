package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Entity;

import javax.annotation.Nonnull;

/**
 * A step to set from what SIDs the permissions will be removed. This class only affects the removal of permissions, for
 * granting or restricting you had to choose the appropriate method in {@link AclAction} which would lead you to the
 * {@link AclTo}.
 *
 * @author stanislav bashkirtsev
 * @see AclAction
 * @see AclTo
 * @see AclFrom
 * @see AclOn
 * @see AclFlush
 * @since 0.13
 */
public interface AclFrom<T extends Entity> {
    /**
     * Defines the SIDs (the object that had permissions) to remove their permissions from the ACL records. The
     * permission record will be removed from database at all.
     *
     * @param sids the objects like users or user groups to remove the permissions from them
     * @return the next step - setting on what object identity the permission being removing was previously set
     */
    AclOn from(@Nonnull T... sids);
}
