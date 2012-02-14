package org.jtalks.common.security.acl;

import org.jtalks.common.model.permissions.BranchPermission;
import org.jtalks.common.model.permissions.JtalksPermission;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;

import java.util.*;

/**
 * Can return {@link JtalksPermission}s by its name or mask. Use {@link #init()} method to initialize the class.
 * Internally it contains a list of permissions which it loads from classes like {@link BranchPermission}, so if you
 * need to add extra permissions, you should change this class to include them. See {@link #init()} method for these
 * purposes.
 *
 * @author stanislav bashkirtsev
 */
public class JtalksPermissionFactory implements PermissionFactory {
    private final Map<Integer, JtalksPermission> permissionsByMask = new HashMap<Integer, JtalksPermission>();
    private final Map<String, JtalksPermission> permissionsByName = new HashMap<String, JtalksPermission>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Permission buildFromMask(int mask) {
        return permissionsByMask.get(mask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Permission buildFromName(String name) {
        return permissionsByName.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Permission> buildFromNames(List<String> names) {
        List<Permission> resultingPermissions = new ArrayList<Permission>();
        for (String name : names) {
            resultingPermissions.add(buildFromName(name));
        }
        return resultingPermissions;
    }

    /**
     * Initializes the class by loading lists of the permissions from classes like {@link BranchPermission}.
     * @return this
     */
    public JtalksPermissionFactory init() {
        List<? extends JtalksPermission> permissions = BranchPermission.getAllAsList();
        for (JtalksPermission permission : permissions) {
            permissionsByMask.put(permission.getMask(), permission);
            permissionsByName.put(permission.getName(), permission);
        }
        return this;
    }

    /**
     * Returns a view of the list of all available permissions.
     *
     * @return a view of the list of all available permissions
     */
    public Collection<? extends JtalksPermission> getAllPermissions() {
        return permissionsByMask.values();
    }
}
