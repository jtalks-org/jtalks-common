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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        //first, we create an empty list for the identities possibly mapped to DTOs
        List<ObjectIdentity> mappedObjects = new ArrayList<ObjectIdentity>(objects.size());
        //reverse mapping: model entity-related identities -> set of DTO-related identities
        Map<ObjectIdentity, Set<ObjectIdentity>> usedMapping = new HashMap<ObjectIdentity, Set<ObjectIdentity>>();
        try {
            for (ObjectIdentity objectIdentity : objects) {
                ObjectIdentity mappedIdentity = getMappedIdentity(objectIdentity);

                //save original mapping - set of all classes that is mapped to this model entity class
                Set<ObjectIdentity> mappedToModelClasses = usedMapping.get(mappedIdentity);
                if (mappedToModelClasses == null) {
                    mappedToModelClasses = new HashSet<ObjectIdentity>();
                }
                mappedToModelClasses.add(objectIdentity);
                usedMapping.put(mappedIdentity, mappedToModelClasses);

                mappedObjects.add(mappedIdentity);
            }

            //get a map [mapped_identity -> acl] from BaseLookupStrategy
            Map<ObjectIdentity, Acl> mappedIdentities = this.lookupStrategy.readAclsById(mappedObjects, sids);
            Map<ObjectIdentity, Acl> acls = new HashMap<ObjectIdentity, Acl>();
            //restore original identities - cast to implementation and use mapped from
            for (ObjectIdentity mappedIdentity : mappedIdentities.keySet()) {
                for (ObjectIdentity mappedToThisIdentity : usedMapping.get(mappedIdentity)) {
                    acls.put(mappedToThisIdentity, mappedIdentities.get(mappedIdentity));
                }
            }
            return acls;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unknown class received from ObjectIdentity.", e);
        }
    }

    /**
     * This method returns {@link ObjectIdentity} mapped to provided one using the following logic:
     * <ul>
     * <li>If no mapping found for the identity type, same object is returned;</li>
     * <li>Instead, a new {@link ObjectIdentity} is created with the type mapped to the type of the original
     * identity and with the same identifier.</li>
     * </ul>
     *
     * @param identity Original identity
     * @return Mapped identity as described above.
     * @throws ClassNotFoundException Any {@link ClassNotFoundException} thrown inside the method.
     */
    private ObjectIdentity getMappedIdentity(ObjectIdentity identity) throws ClassNotFoundException {
        ObjectIdentity result = identity;

        String identityClass = identity.getType();
        Class identityMappedTo = mapper.getMapping(identityClass);
        if (!identityClass.equals(identityMappedTo.getCanonicalName())) {
            result = new ObjectIdentityImpl(identityMappedTo.getCanonicalName(), identity.getIdentifier());
        }

        return result;
    }
}
