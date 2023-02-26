alter table "user"
  rename column phone_number to phone_numbers;
alter table "user"
  alter column phone_numbers type varchar(255) using phone_numbers::varchar(255);

drop index user_phone_number_uindex;

update "user"
set phone_numbers = null;

create extension if not exists pg_trgm;
create index user_search_phone_number_like_index
  on "user" USING gin (phone_numbers gin_trgm_ops);

