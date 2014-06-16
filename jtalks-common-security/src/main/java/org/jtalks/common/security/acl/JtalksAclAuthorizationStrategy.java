package org.jtalks.common.security.acl;

import org.jtalks.common.security.acl.sids.SidFactory;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;

/**
 * @author Mikhail Stryzhonok
 */
public class JtalksAclAuthorizationStrategy extends AclAuthorizationStrategyImpl {
    private SidFactory sidFactory;

    @Override
    protected Sid createCurrentUser(Authentication authentication) {
        return sidFactory.createPrincipal(authentication);
    }

    public SidFactory getSidFactory() {
        return sidFactory;
    }

    public void setSidFactory(SidFactory sidFactory) {
        this.sidFactory = sidFactory;
    }
}
