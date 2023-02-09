update "user"
set "first_name" = ''
where first_name is null;

alter table "user"
  alter column first_name set default '';
alter table "user"
  alter column first_name set not null;

update "user"
set "second_name" = ''
where second_name is null;

alter table "user"
  alter column second_name set default '';
alter table "user"
  alter column second_name set not null;

update "user"
set "third_name" = ''
where third_name is null;

alter table "user"
  alter column third_name set default '';
alter table "user"
  alter column third_name set not null;
