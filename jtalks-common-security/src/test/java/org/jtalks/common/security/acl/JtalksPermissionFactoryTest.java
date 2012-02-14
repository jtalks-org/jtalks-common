package org.jtalks.common.security.acl;

import com.google.common.collect.Lists;
import org.jtalks.common.model.permissions.BranchPermission;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author stanislav bashkirtsev
 */
public class JtalksPermissionFactoryTest {
    JtalksPermissionFactory factory;

    @BeforeMethod
    public void setUp() throws Exception {
        factory = new JtalksPermissionFactory();
        factory.init();
    }

    @Test
    public void testBuildFromMask() throws Exception {
        assertEquals(factory.buildFromMask(BranchPermission.DELETE_POSTS.getMask()), BranchPermission.DELETE_POSTS);
    }

    @Test
    public void testBuildFromName() {
        assertEquals(factory.buildFromName(BranchPermission.VIEW_TOPICS.getName()), BranchPermission.VIEW_TOPICS);
    }

    @Test
    public void testBuildFromNames() throws Exception {
        List<String> names = Lists.newArrayList(
                BranchPermission.VIEW_TOPICS.getName(), BranchPermission.CREATE_TOPICS.getName());
        List<BranchPermission> permissions = Lists.newArrayList(
                BranchPermission.VIEW_TOPICS, BranchPermission.CREATE_TOPICS);
        assertEquals(factory.buildFromNames(names), permissions);
    }

    @Test
    public void testGetAllPermissions() throws Exception {
        assertTrue(BranchPermission.getAllAsList().containsAll(factory.getAllPermissions()));
    }
}
