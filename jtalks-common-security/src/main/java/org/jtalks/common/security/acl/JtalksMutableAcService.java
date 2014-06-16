/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
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
