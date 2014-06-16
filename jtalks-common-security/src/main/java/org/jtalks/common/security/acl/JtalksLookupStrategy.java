package org.jtalks.common.security.acl;

import org.jtalks.common.security.acl.sids.SidFactory;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.model.Sid;

import javax.sql.DataSource;

/**
 * @author Mikhail Stryzhonok
 */
public class JtalksLookupStrategy extends BasicLookupStrategy {

    private SidFactory sidFactory;

    public JtalksLookupStrategy(DataSource dataSource, AclCache aclCache, AclAuthorizationStrategy aclAuthorizationStrategy, PermissionGrantingStrategy grantingStrategy) {
        super(dataSource, aclCache, aclAuthorizationStrategy, grantingStrategy);
    }

    @Override
    protected Sid createSid(boolean isPrincipal, String sid) {
        return sidFactory.create(sid, isPrincipal);
    }

    public SidFactory getSidFactory() {
        return sidFactory;
    }

    public void setSidFactory(SidFactory sidFactory) {
        this.sidFactory = sidFactory;
    }
}
