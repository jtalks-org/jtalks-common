/**
 * Classes within this package relate to Spring ACL and granting/restricting/deleting permissions with handy classes
 * and methods. The main class here is {@link AclBuilders} which can create ACL builders to be used in order to
 * construct granting or any other operation on the permissions. Note, that there are several small interfaces like
 * {@link AclAction} or {@link AclTo} that contain methods related to some particular step in creating full-blown ACL.
 * You should deal with these interfaces again through {@link AclBuilders}.
 */
package org.jtalks.common.security.acl.builders;