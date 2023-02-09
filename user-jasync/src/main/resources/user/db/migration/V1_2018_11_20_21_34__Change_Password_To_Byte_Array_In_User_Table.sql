alter table "user"
  alter column password type bytea using password :: bytea;
