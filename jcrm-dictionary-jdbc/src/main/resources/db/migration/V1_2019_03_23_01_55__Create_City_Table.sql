CREATE TABLE "jcrm-dictionary-db".city
(
  id         bigserial PRIMARY KEY NOT NULL,
  name       varchar(45),
  country_id bigint                NOT NULL
);
CREATE UNIQUE INDEX city_name_and_country_uindex
  ON "jcrm-dictionary-db".city (name, country_id);

ALTER TABLE "jcrm-dictionary-db".city
  ALTER COLUMN name SET NOT NULL;

ALTER TABLE "jcrm-dictionary-db".city
  ADD CONSTRAINT city_to_country_fk
FOREIGN KEY (country_id) REFERENCES "jcrm-dictionary-db".country (id);