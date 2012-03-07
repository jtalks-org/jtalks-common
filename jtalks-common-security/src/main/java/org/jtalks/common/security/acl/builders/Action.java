package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.permissions.JtalksPermission;

/**
 * @author stanislav bashkirtsev
 */
public interface Action<T extends Entity> {
    To<T> grant(JtalksPermission... permissions);

    To<T> restrict(JtalksPermission... permissions);

    From<T> delete(JtalksPermission... permission);

    enum Actions {
        GRANT, DELETE, RESTRICT
    }
}
