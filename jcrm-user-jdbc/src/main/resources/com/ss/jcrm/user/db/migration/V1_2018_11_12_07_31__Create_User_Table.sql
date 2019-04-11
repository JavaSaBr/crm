CREATE TABLE "jcrm-user-db"."user"
(
  id           bigserial PRIMARY KEY,
  name         VARCHAR(100) NOT NULL,
  password     VARCHAR(100) NOT NULL,
  salt         bytea        NOT NULL,
  organization bigint
);
CREATE UNIQUE INDEX user_name_uindex
  ON "jcrm-user-db"."user" (name);