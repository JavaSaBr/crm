create table "user"
(
  id           bigserial primary key,
  name         varchar(100) not null,
  password     varchar(100) not null,
  salt         bytea        not null,
  organization bigint
);
create unique index user_name_uindex
  on "user" (name);
