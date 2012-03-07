package org.jtalks.common.security.acl.builders;

import org.jtalks.common.model.entity.Branch;
import org.jtalks.common.model.entity.User;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.security.acl.AclManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

/**
 * @author stanislav bashkirtsev
 */
public class AclBuildersTest {
    AclManager aclManager;
    AclBuilders builders;

    @BeforeMethod
    public void setUp() throws Exception {
        aclManager = mock(AclManager.class);
        builders = new AclBuilders();
    }

    @Test
    public void testNewBuilder() throws Exception {
        builders.<User>newBuilder(aclManager).grant(GeneralPermission.WRITE)
                .to(new User("", "", "", ""))
                .on(new Branch("", ""))
                .flush();
    }
}
