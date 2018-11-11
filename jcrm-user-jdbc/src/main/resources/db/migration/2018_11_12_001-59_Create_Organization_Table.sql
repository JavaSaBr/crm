CREATE TABLE "jcrm-user-db"."organization"
(
  id bigserial PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX organization_name_uindex ON "jcrm-user-db"."organization" (name);