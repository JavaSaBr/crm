create table email_confirmation
(
  id         bigserial    not null
    constraint email_confirmation_pk
      primary key,
  code       varchar(15)  not null,
  email      varchar(100) not null,
  expiration timestamp    not null
);

create index email_confirmation_code_and_email_index
  on email_confirmation (code, email);
