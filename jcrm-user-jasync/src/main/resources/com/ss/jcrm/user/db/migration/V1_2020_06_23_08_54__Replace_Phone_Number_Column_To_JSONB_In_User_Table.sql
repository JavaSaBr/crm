drop index "jcrm-user-db".user_phone_number_uindex;

alter table "jcrm-user-db"."user"
    drop column phone_number;

alter table "jcrm-user-db"."user"
    add phone_number varchar(255);

