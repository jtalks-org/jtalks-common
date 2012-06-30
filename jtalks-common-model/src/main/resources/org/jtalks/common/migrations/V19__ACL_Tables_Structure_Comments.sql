alter table acl_sid COMMENT 'Contains the list of objects, that get the permissions for something';

alter table acl_sid modify principal tinyint(1) NOT NULL comment 'a label, that shows whether the object is principal(is there a possibility to login with this object or not, i.e. groups cannot be here; true indicates that the sid is a user, false means that the sid is a granted authority)';
alter table acl_sid modify sid varchar(100) NOT NULL comment 'an entity, which get the permissions';
------
alter table acl_class COMMENT 'Contains the list of classes (types of the objects), for which the permissions are given (for JCommune it is the class names)';
------
alter table acl_entry COMMENT 'Contains the logs of security journal. Each log contains the permissions, that is allowed/not allowed for concrete object. Helps to specify what actions can be performed on each of these objects by the desired users';

alter table acl_entry modify acl_object_identity bigint(20) NOT NULL comment 'a link to an an entry in acl_object_identity table - the id of the object';
alter table acl_entry modify ace_order int(11) NOT NULL comment 'an order of permissions priority (AccessControlEntry (ACE) represents individual permission assignment)';
alter table acl_entry modify sid bigint(20) NOT NULL comment 'a link to a acl_sid table - who gets the permissions';
alter table acl_entry modify mask int(11) NOT NULL comment 'bit mask, which identifies the permissions (for example for read, write etc.)';
alter table acl_entry modify granting tinyint(1) NOT NULL comment 'a value, that identifies whether the permission is allowed or not (for JCommune - 1 - allowed, 0 - not allowed). The granting field, if set to true, indicates that the permissions indicated by mask are granted to the corresponding sid, otherwise they are revoked or blocked.';
alter table acl_entry modify audit_success tinyint(1) NOT NULL comment 'a flag to indicate whether to audit a successful permission';
alter table acl_entry modify audit_failure tinyint(1) NOT NULL comment 'a flag to indicate whether to audit a failed permission';
------
alter table acl_object_identity COMMENT 'Each table entry is the representation of the object (in other words - acl_class is class and acl_object_identity is instance of acl_class) for which the permissions are given';

alter table acl_object_identity modify object_id_class bigint(20) NOT NULL comment 'a link to acl_class table - the type of the object';
alter table acl_object_identity modify object_id_identity bigint(20) NOT NULL comment 'an id of the object (for JCommune - the ID of the object)';
alter table acl_object_identity modify parent_object bigint(20) DEFAULT NULL comment 'a link to a parent object';
alter table acl_object_identity modify owner_sid bigint(20) DEFAULT NULL comment 'a link to a acl_sid table - the owner of the object(each object must have an owner). Pay attention if object was inherited - the fields parent_object and entries_inheriting are used to give the due details';
alter table acl_object_identity modify entries_inheriting tinyint(1) NOT NULL comment 'gives the key whether the parent permissions should be extended to children';


