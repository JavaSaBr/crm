CREATE TABLE "jcrm-user-db"."user_role"
(
    id bigserial PRIMARY KEY,
    name varchar(100) NOT NULL
);
CREATE UNIQUE INDEX user_role_name_uindex ON "jcrm-user-db".user_role (name);