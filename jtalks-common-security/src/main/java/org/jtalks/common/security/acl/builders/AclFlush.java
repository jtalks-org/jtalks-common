package org.jtalks.common.security.acl.builders;

/**
 * Represents an action of flushing changes to the ACL structure to the database.
 *
 * @author stanislav bashkirtsev
 * @see AclAction
 * @see AclTo
 * @see AclFrom
 * @see AclOn
 */
public interface AclFlush {
    /**
     * Flushes the changes made during the construction of ACL to the database and cache. This is the last action in the
     * building ACL structure.
     */
    void flush();
}
