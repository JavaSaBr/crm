alter table "jcrm-client-db".contact
    add assigner bigint not null;

alter table "jcrm-client-db".contact
    add curators jsonb default null;

alter table "jcrm-client-db".contact
    add birthday date default null;

alter table "jcrm-client-db".contact
    add phone_numbers jsonb default null;

alter table "jcrm-client-db".contact
    add emails jsonb default null;

alter table "jcrm-client-db".contact
    add sites jsonb default null;

alter table "jcrm-client-db".contact
    add messengers jsonb default null;

alter table "jcrm-client-db".contact
    add company varchar(255) default null;

