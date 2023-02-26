create table "organization"
(
  id   bigserial primary key,
  name varchar(255) not null
);
create unique index organization_name_uindex
  on "organization" (name);
