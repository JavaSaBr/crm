alter table "contact"
  add assigner bigint not null;

alter table "contact"
  add curators jsonb default null;

alter table "contact"
  add birthday date default null;

alter table "contact"
  add phone_numbers jsonb default null;

alter table "contact"
  add emails jsonb default null;

alter table "contact"
  add sites jsonb default null;

alter table "contact"
  add messengers jsonb default null;

alter table "contact"
  add company varchar(255) default null;

