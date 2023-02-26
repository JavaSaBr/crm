create table "industry"
(
  id   bigserial primary key not null,
  name varchar(45)           not null
);
create unique index industry_name_uindex
  on "industry" (name);