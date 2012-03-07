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
