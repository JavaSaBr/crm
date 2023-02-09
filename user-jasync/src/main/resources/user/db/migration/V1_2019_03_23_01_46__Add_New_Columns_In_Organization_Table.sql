alter table organization
  add country_id bigint not null;
alter table organization
  add version int default 0 not null;
alter table organization
  add zip_code varchar(50) null;
alter table organization
  add address varchar(255) null;
alter table organization
  add email varchar(50) null;
alter table organization
  add phone_number varchar(50) null;
alter table organization
  add city_id bigint null;
alter table organization
  add industries jsonb null;
