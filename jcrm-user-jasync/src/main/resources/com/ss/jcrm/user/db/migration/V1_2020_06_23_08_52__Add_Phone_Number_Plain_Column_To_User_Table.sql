alter table "jcrm-user-db"."user"
    add phone_number_plain varchar(45);

create unique index user_phone_number_plain_uindex
    on "jcrm-user-db"."user" (phone_number_plain);
