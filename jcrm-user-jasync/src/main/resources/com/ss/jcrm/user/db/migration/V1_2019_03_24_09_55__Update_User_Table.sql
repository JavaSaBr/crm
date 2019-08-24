ALTER TABLE "jcrm-user-db"."user"
  ADD first_name varchar(45) NULL;
ALTER TABLE "jcrm-user-db"."user"
  ADD second_name varchar(45) NULL;
ALTER TABLE "jcrm-user-db"."user"
  ADD third_name varchar(45) NULL;
ALTER TABLE "jcrm-user-db"."user"
  ADD phone_number varchar(45) NULL;
ALTER TABLE "jcrm-user-db"."user"
  ADD groups jsonb NULL;

ALTER TABLE "jcrm-user-db"."user"
  RENAME COLUMN organization TO organization_id;
ALTER TABLE "jcrm-user-db"."user"
  ADD CONSTRAINT user_to_org_fk
    FOREIGN KEY (organization_id) REFERENCES "jcrm-user-db".organization (id);

alter table "jcrm-user-db"."user"
  alter column version set not null;