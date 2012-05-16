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

import com.google.common.collect.Lists;
import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedList;
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
        factory = new JtalksPermissionFactory().init();
    }

    @Test
    public void testBuildFromMask() throws Exception {
        assertEquals(factory.buildFromMask(BranchPermission.DELETE_OWN_POSTS.getMask()), BranchPermission.DELETE_OWN_POSTS);
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
        List<JtalksPermission> permissions = new LinkedList<JtalksPermission>();
        permissions.addAll(BranchPermission.getAllAsList());
        permissions.addAll(GeneralPermission.getAllAsList());
        assertTrue(permissions.containsAll(factory.getAllPermissions()));
    }
}
