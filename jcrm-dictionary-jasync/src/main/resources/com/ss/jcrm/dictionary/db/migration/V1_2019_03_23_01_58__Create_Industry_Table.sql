CREATE TABLE "jcrm-dictionary-db".industry
(
  id   bigserial PRIMARY KEY NOT NULL,
  name varchar(45)           NOT NULL
);
CREATE UNIQUE INDEX industry_name_uindex
  ON "jcrm-dictionary-db".industry (name);