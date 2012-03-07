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

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Group;
import org.jtalks.common.model.entity.User;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * @author stanislav bashkirtsev
 */
public class JtalksSidFactoryTest {
    private JtalksSidFactory sidFactory;

    @BeforeMethod
    public void setUp() throws Exception {
        sidFactory = new JtalksSidFactory();
    }

    @Test
    public void testCreate_customSidByEntityies() throws Exception {
        Group group = new Group();
        group.setId(1L);
        User user = new User("", "", "", "");
        user.setId(2L);
        List<Entity> receivers = Arrays.asList(group, user);
        List<Sid> sids = sidFactory.create(receivers);
        assertEquals(sids.get(0).getSidId(), "usergroup:1");
        assertEquals(sids.get(1).getSidId(), "user:2");
    }
    
    @Test
    public void testCreate_customSidByEntityt() throws Exception {
        Group receiver = new Group();
        receiver.setId(1L);
        UserGroupSid sid = (UserGroupSid) sidFactory.create(receiver);
        assertEquals(sid.getGroupId(), "1");
        assertEquals(sid.getSidId(), "usergroup:1");
    }

    @Test
    public void testCreate_customSid() throws Exception {
        UserGroupSid sid = (UserGroupSid) sidFactory.create("usergroup:2", true);
        assertEquals(sid.getGroupId(), "2");
        assertEquals(sid.getSidId(), "usergroup:2");
    }

    @Test(expectedExceptions = JtalksSidFactory.SidWithoutRequiredConstructorException.class)
    public void testCreate_sidWithoutRequiredConstructor() throws Exception {
        JtalksSidFactory.addMapping("without_required_constructor", Object.class, SidWithoutRequiredConstructor.class);
        sidFactory.create("without_required_constructor:2", false);
    }

    @Test(expectedExceptions = JtalksSidFactory.SidWithoutRequiredConstructorException.class)
    public void testCreate_sidWithPrivateConstructor() throws Exception {
        JtalksSidFactory.addMapping("private_constructor", Object.class, SidWithPrivateConstructor.class);
        sidFactory.create("private_constructor:2", false);
    }

    @Test(expectedExceptions = JtalksSidFactory.SidClassIsNotConcreteException.class)
    public void testCreate_sidWithAbstractClass() throws Exception {
        JtalksSidFactory.addMapping("abstract_class", Object.class, AbstractSid.class);
        sidFactory.create("abstract_class:2", false);
    }

    @Test(expectedExceptions = JtalksSidFactory.SidConstructorThrewException.class)
    public void testCreate_sidWithConstructorThatThrowsException() throws Exception {
        JtalksSidFactory.addMapping("constructor_throws", Object.class, SidWithConstructorThatThrowsException.class);
        sidFactory.create("constructor_throws:2", false);
    }

    @Test
    public void testCreate_principalSid() throws Exception {
        PrincipalSid sid = (PrincipalSid) sidFactory.create("uncle toby", true);
        assertEquals(sid.getPrincipal(), "uncle toby");
    }

    @Test
    public void testCreate_grantedAuthoritySid() throws Exception {
        GrantedAuthoritySid sid = (GrantedAuthoritySid) sidFactory.create("ROLE_ADMIN", false);
        assertEquals(sid.getGrantedAuthority(), "ROLE_ADMIN");
    }

    private static class SidWithoutRequiredConstructor implements UniversalSid {
        @Override
        public String getSidId() {
            return null;
        }

        @Override
        public boolean isPrincipal() {
            return false;
        }
    }

    private static class SidWithPrivateConstructor implements UniversalSid {
        private SidWithPrivateConstructor(String sidId) {
        }

        @Override
        public boolean isPrincipal() {
            return false;
        }

        @Override
        public String getSidId() {
            return null;
        }
    }

    private static class SidWithConstructorThatThrowsException implements UniversalSid {
        public SidWithConstructorThatThrowsException(String sidId) {
            throw new UnsupportedOperationException(sidId);
        }

        @Override
        public boolean isPrincipal() {
            return false;
        }

        @Override
        public String getSidId() {
            return null;
        }
    }

    private static abstract class AbstractSid implements UniversalSid {
        public AbstractSid(String sidId) {
            super();
        }

        @Override
        public String getSidId() {
            return null;
        }
    }

}
