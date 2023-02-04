ALTER TABLE "jcrm-user-db"."user"
  ALTER COLUMN password TYPE bytea USING password :: bytea;