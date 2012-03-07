package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.security.acl.AclManager;

/**
 * @author stanislav bashkirtsev
 */
public class AclBuilders {
    public <T extends Entity> Action<T> newBuilder(AclManager aclManager){
        return new CompoundAclBuilder<T>(aclManager);
    }
}
