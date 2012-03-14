package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Entity;

import javax.annotation.Nonnull;

/**
 * A step while creating/modifying ACL structure to assign the permissions on some object identity (like branch, topic,
 * post).
 *
 * @author stanislav bashkirtsev
 * @see AclTo
 * @see AclFrom
 * @see AclAction
 * @see AclFlush
 */
public interface AclOn {
    /**
     * This method states for what object the SID will get a permission. Object Identity (or secured object) is always
     * some object SIDs can do something with, e.g. it can be a branch, or a topic, or a post, or anything else.
     *
     * @param objectIdentity the secured object to set permissions to make actions on it
     * @return the next step of the chain - flushing the changes to the database
     */
    AclFlush on(@Nonnull Entity objectIdentity);
}
