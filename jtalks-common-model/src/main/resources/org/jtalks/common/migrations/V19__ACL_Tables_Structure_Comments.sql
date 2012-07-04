alter table acl_sid COMMENT 'list of objects, that get the permissions for something';

alter table acl_sid modify principal tinyint(1) NOT NULL comment 'true - sid is user, false - sid is a granted authority';
alter table acl_sid modify sid varchar(100) NOT NULL comment 'an entity, which get the permissions';
------
alter table acl_class COMMENT 'list of classes, for which the perms are given';
------
alter table acl_entry COMMENT 'logs of security journal';

alter table acl_entry modify acl_object_identity bigint(20) NOT NULL comment 'a link to an an entry in acl_object_identity table';
alter table acl_entry modify ace_order int(11) NOT NULL comment 'AccessControlEntry individual permission assignment order';
alter table acl_entry modify sid bigint(20) NOT NULL comment 'a link to a acl_sid table - who gets the permissions';
alter table acl_entry modify mask int(11) NOT NULL comment 'bit mask, which identifies the permissions (read, write)';
alter table acl_entry modify granting tinyint(1) NOT NULL comment 'whether the permission is allowed or not (revoked, blocked)';
alter table acl_entry modify audit_success tinyint(1) NOT NULL comment 'flag to indicate whether to audit a successful permission';
alter table acl_entry modify audit_failure tinyint(1) NOT NULL comment 'flag to indicate whether to audit a failed permission';
------
alter table acl_object_identity COMMENT 'Each table entry is the representation of the object';

alter table acl_object_identity modify object_id_class bigint(20) NOT NULL comment 'link to acl_class table - the type of the object';
alter table acl_object_identity modify object_id_identity bigint(20) NOT NULL comment 'id of the object (for JCommune - the ID of the object)';
alter table acl_object_identity modify parent_object bigint(20) DEFAULT NULL comment 'link to a parent object';
alter table acl_object_identity modify owner_sid bigint(20) DEFAULT NULL comment 'owner of the object, if object was inherited';
alter table acl_object_identity modify entries_inheriting tinyint(1) NOT NULL comment 'key whether the parent perm should be extended to children';