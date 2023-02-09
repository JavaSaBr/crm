create table user_group
(
  id              bigserial primary key not null,
  name            varchar(45)           not null,
  organization_id bigint                not null,
  constraint user_group_to_org_fk foreign key (organization_id) references organization (id)
);
create unique index user_group_name_and_org_uindex
  on user_group (name, organization_id);

alter table user_group
  add roles jsonb null;

alter table user_group
  add version int default 0 not null;
