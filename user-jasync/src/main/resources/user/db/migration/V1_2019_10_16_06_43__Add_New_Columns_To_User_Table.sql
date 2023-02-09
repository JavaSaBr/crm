alter table "user"
  add created timestamp default null;

alter table "user"
  add modified timestamp default null;

update "user"
set created = now();
update "user"
set modified = now();

alter table "user"
  alter column created set not null;

alter table "user"
  alter column modified set not null;

alter table "user"
  alter column created set default now();

alter table "user"
  alter column modified set default now();
