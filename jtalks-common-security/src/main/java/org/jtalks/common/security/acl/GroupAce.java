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

import org.jtalks.common.model.dao.GroupDao;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.security.acl.sids.UserGroupSid;
import org.springframework.security.acls.model.AccessControlEntry;

/**
 * Created by IntelliJ IDEA. User: ctapobep Date: 1/27/12 Time: 6:53 PM To change this template use File | Settings |
 * File Templates.
 */
public class GroupAce {
    private final AccessControlEntry ace;

    public GroupAce(AccessControlEntry ace) {
        this.ace = ace;
    }

    public Group getGroup(GroupDao groupDao) {
        String groupId = ((UserGroupSid) ace.getSid()).getGroupId();
        Group group = groupDao.get(Long.parseLong(groupId));
        throwIfNull(groupId, group);
        return group;
    }

    public BranchPermission getBranchPermission() {
        return BranchPermission.findByMask(getBranchPermissionMask());
    }

    public int getBranchPermissionMask() {
        return ace.getPermission().getMask();
    }

    public boolean isGranting() {
        return ace.isGranting();
    }

    private void throwIfNull(String groupId, Group group) {
        if (group == null) {
            throw new ObsoleteAclException(groupId);
        }
    }

    public static class ObsoleteAclException extends RuntimeException {

        public ObsoleteAclException(String groupId) {
            super(new StringBuilder("A group with ID [").append(groupId).append("] was removed")
                    .append("but this ID is still registered as a Permission owner (SID) in ACL tables. ")
                    .append("To resolve this issue you should manually remove records from ACL tables ")
                    .append("Note, that this is a bug and this issue should be reported to be corrected in ")
                    .append("future versions.").toString());
        }
    }
}
