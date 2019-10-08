alter table "jcrm-client-db".contact
    add created timestamp default null;

alter table "jcrm-client-db".contact
    add modified timestamp default null;

update "jcrm-client-db".contact set created = now();
update "jcrm-client-db".contact set modified = now();

alter table "jcrm-client-db".contact
    alter column created set not null;

alter table "jcrm-client-db".contact
    alter column modified set not null;

alter table "jcrm-client-db".contact
    alter column created set default now();

alter table "jcrm-client-db".contact
    alter column modified set default now();

