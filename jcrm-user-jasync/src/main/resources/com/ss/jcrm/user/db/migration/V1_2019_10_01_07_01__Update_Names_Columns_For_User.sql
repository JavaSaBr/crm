
update "jcrm-user-db"."user"
set "first_name" = ''
where first_name is null;

alter table "jcrm-user-db"."user"
    alter column first_name set default '';
alter table "jcrm-user-db"."user"
    alter column first_name set not null;

update "jcrm-user-db"."user"
set "second_name" = ''
where second_name is null;

alter table "jcrm-user-db"."user"
    alter column second_name set default '';
alter table "jcrm-user-db"."user"
    alter column second_name set not null;

update "jcrm-user-db"."user"
set "third_name" = ''
where third_name is null;

alter table "jcrm-user-db"."user"
    alter column third_name set default '';
alter table "jcrm-user-db"."user"
    alter column third_name set not null;


