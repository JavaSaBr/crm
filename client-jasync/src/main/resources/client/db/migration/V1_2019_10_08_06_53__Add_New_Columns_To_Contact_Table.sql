alter table "contact"
  add created timestamp default null;

alter table "contact"
  add modified timestamp default null;

update "contact" set created = now();
update "contact" set modified = now();

alter table "contact"
  alter column created set not null;

alter table "contact"
  alter column modified set not null;

alter table "contact"
  alter column created set default now();

alter table "contact"
  alter column modified set default now();

