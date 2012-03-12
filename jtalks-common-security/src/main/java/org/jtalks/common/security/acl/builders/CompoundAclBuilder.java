package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.sids.JtalksSidFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
class CompoundAclBuilder<T extends Entity> implements AclAction<T>, AclTo<T>, AclFrom<T>, AclOn, AclFlush {
    private final List<Permission> permissions = new ArrayList<Permission>();
    private final List<Sid> sids = new ArrayList<Sid>();
    private final AclManager aclManager;
    private JtalksSidFactory sidFactory = new JtalksSidFactory();
    private Entity objectIdentity;
    private Actions action;

    public CompoundAclBuilder(AclManager aclManager) {
        this.aclManager = aclManager;
    }

    @Override
    public AclTo<T> grant(JtalksPermission... permissions) {
        addPermissions(Actions.GRANT, permissions);
        return this;
    }

    @Override
    public AclTo<T> restrict(JtalksPermission... permissions) {
        addPermissions(Actions.RESTRICT, permissions);
        return this;
    }

    @Override
    public AclFrom<T> delete(JtalksPermission... permissions) {
        addPermissions(Actions.DELETE, permissions);
        return this;
    }

    @Override
    public AclOn from(T... sids) {
        this.sids.addAll(sidFactory.create(Arrays.asList(sids)));
        return this;
    }

    @Override
    public AclOn to(T... sids) {
        this.sids.addAll(sidFactory.create(Arrays.asList(sids)));
        return this;
    }


    @Override
    public AclFlush on(Entity objectIdentity) {
        this.objectIdentity = objectIdentity;
        return this;
    }

    @Override
    public void flush() {
        if (action == Actions.GRANT) {
            aclManager.grant(clone(sids), clone(permissions), objectIdentity);
        } else if (action == Actions.RESTRICT) {
            aclManager.restrict(clone(sids), clone(permissions), objectIdentity);
        } else {
            aclManager.delete(clone(sids), clone(permissions), objectIdentity);
        }
    }


    private void addPermissions(Actions action, JtalksPermission... permissions) {
        this.permissions.addAll(Arrays.asList(permissions));
        this.action = action;
    }

    private <T> List<T> clone(List<T> list) {
        return new ArrayList<T>(list);
    }
}
