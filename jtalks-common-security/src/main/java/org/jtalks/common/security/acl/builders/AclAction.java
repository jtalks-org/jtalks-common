package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.permissions.JtalksPermission;

/**
 * @author stanislav bashkirtsev
 */
public interface AclAction<T extends Entity> {
    AclTo<T> grant(JtalksPermission... permissions);

    AclTo<T> restrict(JtalksPermission... permissions);

    AclFrom<T> delete(JtalksPermission... permission);

    enum Actions {
        GRANT, DELETE, RESTRICT
    }
}
