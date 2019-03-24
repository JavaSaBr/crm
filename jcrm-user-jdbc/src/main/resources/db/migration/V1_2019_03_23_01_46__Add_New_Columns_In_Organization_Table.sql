ALTER TABLE "jcrm-user-db".organization
  ADD country_id bigint NOT NULL;
ALTER TABLE "jcrm-user-db".organization
  ADD version int DEFAULT 0 NOT NULL;
ALTER TABLE "jcrm-user-db".organization
  ADD zip_code varchar(50) NULL;
ALTER TABLE "jcrm-user-db".organization
  ADD address varchar(255) NULL;
ALTER TABLE "jcrm-user-db".organization
  ADD email varchar(50) NULL;
ALTER TABLE "jcrm-user-db".organization
  ADD phone_number varchar(50) NULL;
ALTER TABLE "jcrm-user-db".organization
  ADD city_id bigint NULL;
ALTER TABLE "jcrm-user-db".organization
  ADD industries jsonb NULL;