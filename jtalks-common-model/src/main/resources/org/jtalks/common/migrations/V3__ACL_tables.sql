SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
set foreign_key_checks=0;

--
-- Table structure for table `acl_sid`
--
-- Contains the list of objects, that get the permissions for something
-- (for JCommune - groups and users)
--
DROP TABLE IF EXISTS `acl_sid`;
CREATE TABLE `acl_sid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `principal` tinyint(1) NOT NULL,                         -- a label, that shows whether the object is principal(is there a possibility to login with this object or not, i.e. groups cannot be here; true indicates that the sid is a user, false means that the sid is a granted authority)
  `sid` varchar(100) NOT NULL,                             -- an entity, which get the permissions()
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_acl_sid` (`sid`,`principal`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `acl_class`
--
-- Contains the list of classes (types of the objects), for which the permissions are given
-- (for JCommune it is the class names)
--
DROP TABLE IF EXISTS `acl_class`;
CREATE TABLE `acl_class` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_acl_class` (`class`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `acl_entry`
--
-- Contains the logs of security journal. Each log contains the permissions, that is allowed/not allowed for concrete object
-- Helps to specify what actions can be performed on each of these objects by the desired users
--
DROP TABLE IF EXISTS `acl_entry`;
CREATE TABLE `acl_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `acl_object_identity` bigint(20) NOT NULL,                      -- a link to an an entry in acl_object_identity table - the id of the object
  `ace_order` int(11) NOT NULL,                                   -- an order of permissions priority (AccessControlEntry (ACE) represents individual permission assignment)
  `sid` bigint(20) NOT NULL,                                      -- a link to a acl_sid table - who gets the permissions
  `mask` int(11) NOT NULL,                                        -- bit mask, which identifies the permissions (for example for read, write etc.)
  `granting` tinyint(1) NOT NULL,                                 -- a value, that identifies whether the permission is allowed or not (for JCommune - 1 - allowed, 0 - not allowed). The granting field, if set to true, indicates that the permissions indicated by mask are granted to the corresponding sid, otherwise they are revoked or blocked.
  `audit_success` tinyint(1) NOT NULL,                            -- a flag to indicate whether to audit a successful permission
  `audit_failure` tinyint(1) NOT NULL,                            -- a flag to indicate whether to audit a failed permission
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_acl_entry` (`acl_object_identity`,`ace_order`),
  KEY `fk_acl_entry_obj_id` (`acl_object_identity`),
  KEY `fk_acl_entry_sid` (`sid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `acl_object_identity`
--
-- Each table entry is the representation of the object (in other words - acl_class is class and acl_object_identity is instance of acl_class).
-- for which the permissions are given
--
DROP TABLE IF EXISTS `acl_object_identity`;
CREATE TABLE `acl_object_identity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `object_id_class` bigint(20) NOT NULL,                              -- a link to acl_class table - the type of the object
  `object_id_identity` bigint(20) NOT NULL,                           -- an id of the object (for JCommune - the ID of the object)
  `parent_object` bigint(20) DEFAULT NULL,                            -- a link to a parent object
  `owner_sid` bigint(20) DEFAULT NULL,                                -- a link to a acl_sid table - the owner of the object(each object must have an owner). Pay attention if object was inherited - the fields parent_object and entries_inheriting are used to give the due details
  `entries_inheriting` tinyint(1) NOT NULL,                           -- gives the key whether the parent permissions should be extended to children
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_acl_objid` (`object_id_class`,`object_id_identity`),
  KEY `fk_acl_obj_parent` (`parent_object`),
  KEY `fk_acl_obj_class` (`object_id_class`),
  KEY `fk_acl_obj_owner` (`owner_sid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Constraints for dumped tables
--

--
-- Constraints for table `acl_entry`
--
ALTER TABLE `acl_entry`
  ADD CONSTRAINT `fk_acl_entry_obj_id` FOREIGN KEY (`acl_object_identity`) REFERENCES `acl_object_identity` (`id`),
  ADD CONSTRAINT `fk_acl_entry_sid` FOREIGN KEY (`sid`) REFERENCES `acl_sid` (`id`);

--
-- Constraints for table `acl_object_identity`
--
ALTER TABLE `acl_object_identity`
  ADD CONSTRAINT `fk_acl_obj_parent` FOREIGN KEY (`parent_object`) REFERENCES `acl_object_identity` (`id`),
  ADD CONSTRAINT `fk_acl_obj_class` FOREIGN KEY (`object_id_class`) REFERENCES `acl_class` (`id`),
  ADD CONSTRAINT `fk_acl_obj_owner` FOREIGN KEY (`owner_sid`) REFERENCES `acl_sid` (`id`);

set foreign_key_checks=1;
