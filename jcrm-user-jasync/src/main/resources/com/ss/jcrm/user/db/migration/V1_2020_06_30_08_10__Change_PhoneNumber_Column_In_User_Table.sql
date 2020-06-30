alter table "jcrm-user-db"."user" rename column phone_number to phone_numbers;
alter table "jcrm-user-db"."user" alter column phone_numbers type varchar(255) using phone_numbers::varchar(255);

drop index "jcrm-user-db".user_phone_number_uindex;

create extension if not exists pg_trgm;
create index user_search_phone_number_like_index
    on "jcrm-user-db"."user" USING gin (phone_numbers gin_trgm_ops);

