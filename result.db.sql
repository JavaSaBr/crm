create schema "jcrm-dictionary-db";

alter schema "jcrm-dictionary-db" owner to postgres;

create table country
(
    id         bigserial   not null
        constraint country_pkey
            primary key,
    name       varchar(45) not null,
    flag_code  varchar(25) not null,
    phone_code varchar(10) not null
);

alter table country
    owner to postgres;

create index country__name_index
    on country (name);

create table city
(
    id         bigserial   not null
        constraint city_pkey
            primary key,
    name       varchar(45) not null,
    country_id bigint      not null
        constraint city_to_country_fk
            references country
);

alter table city
    owner to postgres;

create unique index city_name_and_country_uindex
    on city (name, country_id);

create table industry
(
    id   bigserial   not null
        constraint industry_pkey
            primary key,
    name varchar(45) not null
);

alter table industry
    owner to postgres;

create unique index industry_name_uindex
    on industry (name);


create schema "jcrm-user-db";

alter schema "jcrm-user-db" owner to postgres;

create table organization
(
    id           bigserial         not null
        constraint organization_pkey
            primary key,
    name         varchar(255)      not null,
    country_id   bigint            not null,
    version      integer default 0 not null,
    zip_code     varchar(50),
    address      varchar(255),
    email        varchar(50),
    phone_number varchar(50),
    city_id      bigint,
    industries   jsonb
);

alter table organization
    owner to postgres;

create unique index organization_name_uindex
    on organization (name);

create table "user"
(
    id              bigserial             not null
        constraint user_pkey
            primary key,
    name            varchar(100)          not null,
    password        bytea                 not null,
    salt            bytea                 not null,
    organization_id bigint
        constraint user_to_org_fk
            references organization,
    roles           jsonb,
    version         integer default 0     not null,
    first_name      varchar(45),
    second_name     varchar(45),
    third_name      varchar(45),
    phone_number    varchar(45),
    groups          jsonb,
    email_confirmed boolean default false not null
);

alter table "user"
    owner to postgres;

create unique index user_name_uindex
    on "user" (name);

create table user_group
(
    id              bigserial         not null
        constraint user_group_pkey
            primary key,
    name            varchar(45)       not null,
    organization_id bigint            not null
        constraint user_group_to_org_fk
            references organization,
    roles           jsonb,
    version         integer default 0 not null
);

alter table user_group
    owner to postgres;

create unique index user_group_name_and_org_uindex
    on user_group (name, organization_id);

create table email_confirmation
(
    id         bigserial    not null
        constraint email_confirmation_pk
            primary key,
    code       varchar(15)  not null,
    email      varchar(100) not null,
    expiration timestamp    not null
);

alter table email_confirmation
    owner to postgres;

create index email_confirmation_code_and_email_index
    on email_confirmation (code, email);

