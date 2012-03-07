package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Entity;

/**
 * @author stanislav bashkirtsev
 */
public interface AclTo<T extends Entity> {
    AclOn to(T... sids);
}
