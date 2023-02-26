create table "contact"
(
  id          bigserial         not null
    constraint contact_pk
      primary key,
  org_id      bigint            not null,
  first_name  varchar(45),
  second_name varchar(45),
  third_name  varchar(45),
  version     integer default 0 not null
);

