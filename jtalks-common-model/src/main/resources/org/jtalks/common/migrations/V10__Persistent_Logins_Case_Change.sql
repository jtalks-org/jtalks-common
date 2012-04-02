alter table PERSISTENT_LOGINS rename persistent_logins;
alter table persistent_logins change USERNAME username varchar(64) not null;
alter table persistent_logins change SERIES series varchar(64) not null;
alter table persistent_logins change TOKEN token varchar(64) not null;
alter table persistent_logins change LAST_USED last_used timestamp not null;
alter table persistent_logins ENGINE = InnoDB;
alter table persistent_logins DEFAULT CHARACTER SET utf8;

