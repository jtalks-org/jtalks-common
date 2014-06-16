package org.jtalks.common.security.acl;

import org.jtalks.common.security.acl.sids.UniversalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * @author Mikhail Stryzhonok
 */
public class JtalksMutableAcService extends JdbcMutableAclService {

    public JtalksMutableAcService(DataSource dataSource, LookupStrategy lookupStrategy, AclCache aclCache) {
        super(dataSource, lookupStrategy, aclCache);
    }

    @Override
    protected Long createOrRetrieveSidPrimaryKey(Sid sid, boolean allowCreate) {
        Assert.notNull(sid, "Sid required");
        Assert.isInstanceOf(UniversalSid.class, sid, "Unsupported sid implementation");

        String sidId = ((UniversalSid) sid).getSidId();
        boolean isPrinciple = ((UniversalSid) sid).isPrincipal();
        return createOrRetrieveSidPrimaryKey(sidId, isPrinciple, allowCreate);
    }

    @Override
    protected String getSidId(Sid sid) {
        Assert.notNull(sid, "Sid required");
        Assert.isInstanceOf(UniversalSid.class, sid, "Unsupported sid implementation");
        return ((UniversalSid) sid).getSidId();
    }
}
