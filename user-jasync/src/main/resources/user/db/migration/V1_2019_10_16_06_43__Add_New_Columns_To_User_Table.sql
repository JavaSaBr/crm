alter table "jcrm-user-db"."user"
    add created timestamp default null;

alter table "jcrm-user-db"."user"
    add modified timestamp default null;

update "jcrm-user-db"."user" set created = now();
update "jcrm-user-db"."user" set modified = now();

alter table "jcrm-user-db"."user"
    alter column created set not null;

alter table "jcrm-user-db"."user"
    alter column modified set not null;

alter table "jcrm-user-db"."user"
    alter column created set default now();

alter table "jcrm-user-db"."user"
    alter column modified set default now();

