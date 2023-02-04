CREATE TABLE "jcrm-user-db".user_group
(
  id              bigserial PRIMARY KEY NOT NULL,
  name            varchar(45)           NOT NULL,
  organization_id bigint                NOT NULL,
  CONSTRAINT user_group_to_org_fk FOREIGN KEY (organization_id) REFERENCES "jcrm-user-db".organization (id)
);
CREATE UNIQUE INDEX user_group_name_and_org_uindex
  ON "jcrm-user-db".user_group (name, organization_id);

ALTER TABLE "jcrm-user-db".user_group
  ADD roles jsonb NULL;

alter table "jcrm-user-db".user_group
  add version int default 0 not null;