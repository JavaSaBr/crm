create table "country"
(
  id         bigserial primary key not null,
  name       varchar(45)           not null,
  flag_code  varchar(25)           not null,
  phone_code varchar(10)           not null
);
create unique index country__name_uniq_index
  on "country" ("name");