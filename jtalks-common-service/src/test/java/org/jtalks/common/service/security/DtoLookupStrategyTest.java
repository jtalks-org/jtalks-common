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
package org.jtalks.common.service.security;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * <p>This class contains unit tests for {@link DtoLookupStrategy}.</p>
 * Date: 16.09.2011<br />
 * Time: 16:06
 *
 * @author Alexey Malev
 */
public class DtoLookupStrategyTest {

    private DtoMapper mapper;
    private LookupStrategy strategy;
    private List<ObjectIdentity> stragegyCallArgument;
    private List<ObjectIdentity> objects;
    List<Sid> sids;
    private DtoLookupStrategy sut;

    private class c1 {

    }

    private class c2 {

    }

    private class c3 {

    }

    @BeforeMethod
    public void setUp() {
        mapper = mock(DtoMapper.class);
        strategy = mock(LookupStrategy.class);
        when(strategy.readAclsById(anyListOf(ObjectIdentity.class), anyListOf(Sid.class))).thenAnswer(
            new Answer<Map<ObjectIdentity, Acl>>() {
                @Override
                public Map<ObjectIdentity, Acl> answer(InvocationOnMock invocation) throws Throwable {
                    stragegyCallArgument = (List<ObjectIdentity>) invocation.getArguments()[0];
                    Map<ObjectIdentity, Acl> result = new HashMap<ObjectIdentity, Acl>();
                    result.put(stragegyCallArgument.get(0), null);

                    return result;
                }
            });

        ObjectIdentity identity = mock(ObjectIdentity.class);
        when(identity.getType()).thenReturn(c1.class.getCanonicalName());
        when(identity.getIdentifier()).thenReturn(1L);

        objects = new ArrayList<ObjectIdentity>();
        objects.add(identity);

        sids = new ArrayList<Sid>();

        sut = new DtoLookupStrategy(strategy, mapper);
    }

    @Test
    public void testReadAclsByIdUsingMapping() {
        when(mapper.getMapping(c1.class.getCanonicalName())).thenReturn(c2.class);

        ObjectIdentity mappedIdentity = mock(ObjectIdentity.class);
        when(mappedIdentity.getType()).thenReturn(c2.class.getCanonicalName());
        when(mappedIdentity.getIdentifier()).thenReturn(1L);
        List<ObjectIdentity> mappedObjects = new ArrayList<ObjectIdentity>();
        mappedObjects.add(mappedIdentity);

        Map<ObjectIdentity, Acl> result = sut.readAclsById(objects, sids);

        ObjectIdentity actualArgument = stragegyCallArgument.get(0);
        assertEquals(actualArgument.getType(), mappedIdentity.getType());
        assertEquals(actualArgument.getIdentifier(), mappedIdentity.getIdentifier());
        assertEquals(result.keySet().iterator().next().getType(), c1.class.getCanonicalName());
    }

    @Test
    public void testMultipleDtoOnSameEntityMappings() {
        when(mapper.getMapping(c1.class.getCanonicalName())).thenReturn(c2.class);
        when(mapper.getMapping(c3.class.getCanonicalName())).thenReturn(c2.class);

        ObjectIdentity mappedIdentity = mock(ObjectIdentity.class);
        when(mappedIdentity.getType()).thenReturn(c1.class.getCanonicalName());
        when(mappedIdentity.getIdentifier()).thenReturn(1L);
        ObjectIdentity secondMappedIdentity = mock(ObjectIdentity.class);
        when(secondMappedIdentity.getType()).thenReturn(c3.class.getCanonicalName());
        when(secondMappedIdentity.getIdentifier()).thenReturn(1L);
        List<ObjectIdentity> mappedObjects = new ArrayList<ObjectIdentity>();
        mappedObjects.add(mappedIdentity);
        mappedObjects.add(secondMappedIdentity);

        Map<ObjectIdentity, Acl> result = sut.readAclsById(mappedObjects, sids);

        assertEquals(stragegyCallArgument.size(), mappedObjects.size());
        assertEquals(stragegyCallArgument.get(0).getType(), c2.class.getCanonicalName());
        assertEquals(stragegyCallArgument.get(1).getType(), c2.class.getCanonicalName());
        assertTrue(result.containsKey(mappedIdentity));
        assertTrue(result.containsKey(secondMappedIdentity));
    }

    @Test
    public void testReadAclsByIdWithoutMapping() {
        Map<ObjectIdentity, Acl> result = sut.readAclsById(objects, sids);

        verify(strategy).readAclsById(anyListOf(ObjectIdentity.class), anyListOf(Sid.class));
        assertEquals(result.keySet().iterator().next().getType(), c1.class.getCanonicalName());
    }
}
