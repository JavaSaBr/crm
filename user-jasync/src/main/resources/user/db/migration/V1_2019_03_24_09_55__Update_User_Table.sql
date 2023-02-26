alter table "user"
  add first_name varchar(45) null;
alter table "user"
  add second_name varchar(45) null;
alter table "user"
  add third_name varchar(45) null;
alter table "user"
  add phone_number varchar(45) null;
alter table "user"
  add groups jsonb null;

alter table "user"
  rename column organization to organization_id;
alter table "user"
  add constraint user_to_org_fk
    foreign key (organization_id) references organization (id);

alter table "user"
  alter column version set not null;
