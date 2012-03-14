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
package org.jtalks.common.security;

import org.jtalks.common.model.dao.UserDao;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.security.acl.AclManager;
import org.jtalks.common.security.acl.BasicAclBuilder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @author stanislav bashkirtsev
 */
public class SecurityServiceTest {
    UserDao userDao;
    AclManager aclManager;
    SecurityService securityService;

    @BeforeMethod
    public void setUp() throws Exception {
        userDao = mock(UserDao.class);
        aclManager = mock(AclManager.class);
        securityService = new SecurityService(userDao, aclManager);
    }

    @Test
    public void testGrantToCurrentUser() throws Exception {
        SecurityService spySut = spy(securityService);
        doReturn(new User("", "","", "")).when(spySut).getCurrentUser();
        BasicAclBuilder aclBuilder = spySut.grantToCurrentUser(GeneralPermission.WRITE);

    }
}
