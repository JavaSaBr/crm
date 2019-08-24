create table "jcrm-user-db".email_confirmation
(
  id         bigserial    not null
    constraint email_confirmation_pk
      primary key,
  code       VARCHAR(15)  not null,
  email      VARCHAR(100) not null,
  expiration TIMESTAMP    not null
);

create index email_confirmation_code_and_email_index
  on "jcrm-user-db".email_confirmation (code, email);

