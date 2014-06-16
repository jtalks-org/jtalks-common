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

import org.jtalks.common.model.entity.Branch;
import org.jtalks.common.model.entity.Component;
import org.springframework.security.acls.model.ObjectIdentity;
import org.testng.annotations.Test;

import static org.jtalks.common.security.acl.TypeConvertingObjectIdentityGenerator.createDefaultGenerator;
import static org.testng.Assert.assertEquals;

/**
 * @author stanislav bashkirtsev
 */
public class TypeConvertingObjectIdentityGeneratorTest {

    @Test
    public void createObjectIdentityShouldNotConvertType() throws Exception {
        TypeConvertingObjectIdentityGenerator sut = createDefaultGenerator();
        ObjectIdentity oid = sut.createObjectIdentity(1L, Branch.class.getSimpleName());
        assertEquals(Branch.class.getSimpleName(), oid.getType());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createObjectIdentityShouldThrowIfIdIsZero() throws Exception {
        createDefaultGenerator().createObjectIdentity(0, "");
    }

    @Test
    public void getObjectIdentityShouldApplySameRuleForSubclass(){
        TypeConvertingObjectIdentityGenerator sut = createDefaultGenerator();
        ObjectIdentity objectIdentity = sut.getObjectIdentity(new BranchOffspring());
        assertEquals(objectIdentity.getType(), "BRANCH");
    }

    @Test
    public void getObjectIdentityShouldNotConvertWithEmptyRules() throws Exception {
        TypeConvertingObjectIdentityGenerator sut = new TypeConvertingObjectIdentityGenerator();
        Branch domainObject = givenPersistedBranch();

        ObjectIdentity objectIdentity = sut.getObjectIdentity(domainObject);
        assertEquals(Branch.class.getSimpleName(), objectIdentity.getType());
        assertEquals(domainObject.getId(), objectIdentity.getIdentifier());
    }

    @Test
    public void getObjectIdentityShouldConvertIfRuleIsSet() throws Exception {
        TypeConvertingObjectIdentityGenerator sut = createDefaultGenerator();
        Branch domainObject = givenPersistedBranch();

        ObjectIdentity objectIdentity = sut.getObjectIdentity(domainObject);
        assertEquals("BRANCH", objectIdentity.getType());
        assertEquals(domainObject.getId(), objectIdentity.getIdentifier());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getObjectIdentityShouldThrowIfEntityIsNotPersisted() throws Exception {
        new TypeConvertingObjectIdentityGenerator().getObjectIdentity(new Component());
    }

    private Branch givenPersistedBranch() {
        Branch domainObject = new Branch("", "");
        domainObject.setId(1L);
        return domainObject;
    }

    /**
     * To test that conversion rules are applied for the children of converted class, we need to subclass one of
     * entities.
     */
    private static class BranchOffspring extends Branch {
        private BranchOffspring() {
            setId(10L);
        }
    }
}
