create table "city"
(
  id         bigserial primary key not null,
  name       varchar(45),
  country_id bigint                not null
);
create unique index city_name_and_country_uindex
  on "city" (name, country_id);

alter table "city"
  alter column name set not null;

alter table "city"
  add constraint city_to_country_fk
    foreign key (country_id) references "country" (id);