create extension if not exists pg_trgm;
create index user_search_full_name__like_index
  on "user" using gin ((first_name || ' ' || second_name || ' ' || third_name) gin_trgm_ops);
create index user_search_email_like_index
  on "user" USING gin (email gin_trgm_ops);
