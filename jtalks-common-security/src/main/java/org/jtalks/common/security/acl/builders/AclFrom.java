package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Entity;

/**
 * @author stanislav bashkirtsev
 */
public interface AclFrom<T extends Entity> {
    AclOn from(T... sids);
}
