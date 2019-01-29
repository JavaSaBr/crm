CREATE TABLE "jcrm-dictionary-db"."country"
(
  id         bigserial PRIMARY KEY NOT NULL,
  name       varchar(45)           NOT NULL,
  flag_code  varchar(25)           NOT NULL,
  phone_code varchar(10)           NOT NULL
);
CREATE UNIQUE INDEX country__name_uniq_index
  ON "jcrm-dictionary-db"."country" ("name");