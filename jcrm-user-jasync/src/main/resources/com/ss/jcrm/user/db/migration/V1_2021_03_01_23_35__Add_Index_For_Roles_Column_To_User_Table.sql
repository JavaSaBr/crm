create
    index user_roles_index on "jcrm-user-db"."user" using gin (roles);
