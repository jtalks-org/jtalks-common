/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.common.service.security;

import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>This implementation of the {@link LookupStrategy} is very similar to {@link BasicLookupStrategy},
 * except for object identity classes are being replaced with their mappings, if any.</p>
 * See {@link DtoLookupStrategy#readAclsById(List, List)} for details.
 * <p/>
 * Date: 16.09.2011<br />
 * Time: 15:07
 *
 * @author Alexey Malev
 */
public class DtoLookupStrategy implements LookupStrategy {

    private DtoMapper mapper;
    private LookupStrategy lookupStrategy;


    /**
     * Default constructor for the strategy.
     *
     * @param lookupStrategy Lookup Strategy used to delegate lookup.
     * @param mapper         Mapper used to retrieve mapped model classes.
     */
    public DtoLookupStrategy(LookupStrategy lookupStrategy, DtoMapper mapper) {
        this.lookupStrategy = lookupStrategy;
        this.mapper = mapper;
    }

    /**
     * <p>This method passes over all provided objects and replace them with their mappings, if ones exists,
     * before further processing performed by BasicLookupStrategy.</p>
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) {

        List<ObjectIdentity> mappedObjects = new ArrayList<ObjectIdentity>(objects.size());
        try {
            for (ObjectIdentity objectIdentity : objects) {
                String identityClass = objectIdentity.getType();
                Class identityMappedTo = mapper.getMapping(identityClass);
                if (identityClass.equals(identityMappedTo.getCanonicalName())) {
                    mappedObjects.add(objectIdentity);
                } else {
                    mappedObjects.add(
                          new ObjectIdentityImpl(identityMappedTo.getCanonicalName(), objectIdentity.getIdentifier()));
                }
            }

            return this.lookupStrategy.readAclsById(mappedObjects, sids);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unknown class received from ObjectIdentity.", e);
        }
    }
}
