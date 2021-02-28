alter table "jcrm-user-db".user_group
    add modified timestamp default now() not null,
    add created  timestamp default now() not null;

