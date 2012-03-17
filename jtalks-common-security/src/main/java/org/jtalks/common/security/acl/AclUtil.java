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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import org.jtalks.common.model.entity.Entity;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

/**
 * The fine grained utilities to work with Spring ACL.
 *
 * @author stanislav bashkirtsev
 */
public class AclUtil implements ObjectIdentities, Acls, Permissions {
    private ObjectIdentityGenerator objectIdentityGenerator = new ObjectIdentityRetrievalStrategyImpl();
    private final MutableAclService mutableAclService;

    /**
     * Use this constructor if you need a full blown ACL utilities. If you need to work with only object identities or
     * permissions, use respective factory methods.
     *
     * @param mutableAclService the acl service that is required for some operations related to DB. If you use factory
     *                          methods, then you don't need to specify it working with object identities only for
     *                          example.
     * @see #createObjectIdentityUtils()
     * @see #createAclUtils(org.springframework.security.acls.model.MutableAclService)
     * @see #createPermissionUtils(org.springframework.security.acls.model.MutableAclService)
     */
    public AclUtil(@Nonnull MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    /**
     * Need to be able to create it without {@link MutableAclService} if it's not required. This was created mainly for
     * the purpose of factory methods.
     */
    AclUtil() {
        this.mutableAclService = null;
    }

    /**
     * Creates a new instance of the object identity utilities (it won't have methods related to {@link Permission} or
     * {@link Acl}.
     *
     * @return new instance of the object identity utilities
     */
    public static ObjectIdentities createObjectIdentityUtils() {
        return new AclUtil();
    }

    /**
     * Creates a new instance of the ACL utilities (it won't work with {@link ObjectIdentity} or {@link Permission}).
     *
     * @param mutableAclService the service to get database/cache access
     * @return instance of the ACL utilities
     */
    public static Acls createAclUtils(@Nonnull MutableAclService mutableAclService) {
        return new AclUtil(mutableAclService);
    }

    /**
     * Creates a new instance of permission utilities (it won't work with {@link ObjectIdentity}; it will work with
     * {@link Acl} though, but the main purpose of these utilities is to work mainly with graning/deleting/restricting
     * permissions).
     *
     * @param mutableAclService the service to get database/cache access
     * @return instance of permission utilities
     */
    public static Permissions createPermissionUtils(@Nonnull MutableAclService mutableAclService) {
        return new AclUtil();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedMutableAcl getAclFor(Entity entity) {
        ObjectIdentity oid = createIdentityFor(entity);
        return getAclFor(oid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedMutableAcl getAclFor(ObjectIdentity oid) {
        try {
            return ExtendedMutableAcl.castAndCreate(mutableAclService.readAclById(oid));
        } catch (NotFoundException nfe) {
            return ExtendedMutableAcl.castAndCreate(mutableAclService.createAcl(oid));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectIdentity createIdentityFor(Entity securedObject) {
        if (securedObject.getId() <= 0) {
            throw new IllegalArgumentException("Object id must be assigned before creating acl.");
        }
        return new ObjectIdentityImpl(securedObject.getClass(), securedObject.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectIdentity createIdentity(@Nonnull Serializable id, @Nonnull String type) {
        return objectIdentityGenerator.createObjectIdentity(id, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedMutableAcl grant(List<? extends Sid> sids, List<Permission> permissions, Entity target) {
        return applyPermissionsToSids(sids, permissions, target, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedMutableAcl restrict(List<? extends Sid> sids, List<Permission> permissions, Entity target) {
        return applyPermissionsToSids(sids, permissions, target, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedMutableAcl delete(List<? extends Sid> sids, List<Permission> permissions, Entity target) {
        ObjectIdentity oid = createIdentityFor(target);
        ExtendedMutableAcl acl = ExtendedMutableAcl.castAndCreate(mutableAclService.readAclById(oid));
        deletePermissionsFromAcl(acl, sids, permissions);
        return acl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePermissionsFromAcl(
            ExtendedMutableAcl acl, List<? extends Sid> sids, List<Permission> permissions) {
        List<AccessControlEntry> allEntries = acl.getEntries(); // it's a copy
        List<AccessControlEntry> filtered = newArrayList(filter(allEntries, new BySidAndPermissionFilter(sids, permissions)));
        acl.delete(filtered);
    }
    
    public Acl aclFromObjectIdentity(@Nonnull Serializable id, @Nonnull String type){
        ObjectIdentity identity = this.objectIdentityGenerator.createObjectIdentity(id, type);
        return getAclFor(identity);
    }

    @VisibleForTesting
    void setObjectIdentityGenerator(ObjectIdentityGenerator objectIdentityGenerator) {
        this.objectIdentityGenerator = objectIdentityGenerator;
    }


    /**
     * Apply every permission from list to every sid from list.
     *
     * @param sids        list of sids
     * @param permissions list of permissions
     * @param target      securable object
     * @param granting    grant if true, restrict if false
     * @return the ACL that manages the specified {@code target} and its Sids & Permissions
     */
    private ExtendedMutableAcl applyPermissionsToSids(
            List<? extends Sid> sids, List<Permission> permissions, Entity target, boolean granting) {
        ExtendedMutableAcl acl = getAclFor(target);
        deletePermissionsFromAcl(acl, sids, permissions);
        acl.addPermissions(sids, permissions, granting);
        return acl;
    }


    /**
     * Gets the list of Sids and Permissions into the constructor and filters out those {@link AccessControlEntry} whose
     * Sid & Permission is not in the specified lists.
     *
     * @see com.google.common.collect.Iterators#filter(java.util.Iterator, com.google.common.base.Predicate)
     */
    private static class BySidAndPermissionFilter implements Predicate<AccessControlEntry> {
        private final List<? extends Sid> sids;
        private final List<Permission> permissions;

        /**
         * @param sids        to find {@link AccessControlEntry}s that contain them
         * @param permissions to find the {@link AccessControlEntry}s where specified {@code sids} have these
         *                    permissions
         */
        private BySidAndPermissionFilter(@Nonnull List<? extends Sid> sids, @Nonnull List<Permission> permissions) {
            this.sids = sids;
            this.permissions = permissions;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean apply(@Nullable AccessControlEntry input) {
            if (input == null) {
                return false;
            }
            return sids.contains(input.getSid()) && permissions.contains(input.getPermission());
        }

        /**
         * Always return {@code false}, we don't need this functionality.
         *
         * @param object who cares
         * @return always {@code false}
         */
        @Override
        public boolean equals(@Nullable Object object) {
            return false;
        }
    }
}