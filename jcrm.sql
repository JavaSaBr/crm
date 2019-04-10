--
-- PostgreSQL database dump
--

-- Dumped from database version 10.6 (Ubuntu 10.6-0ubuntu0.18.04.1)
-- Dumped by pg_dump version 10.6 (Ubuntu 10.6-0ubuntu0.18.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE IF EXISTS jcrm;
--
-- Name: jcrm; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE jcrm WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'ru_RU.UTF-8' LC_CTYPE = 'ru_RU.UTF-8';


ALTER DATABASE jcrm OWNER TO postgres;

\connect jcrm

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: jcrm-dictionary-db; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "jcrm-dictionary-db";


ALTER SCHEMA "jcrm-dictionary-db" OWNER TO postgres;

--
-- Name: jcrm-user-db; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "jcrm-user-db";


ALTER SCHEMA "jcrm-user-db" OWNER TO postgres;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: city; Type: TABLE; Schema: jcrm-dictionary-db; Owner: postgres
--

CREATE TABLE "jcrm-dictionary-db".city (
    id bigint NOT NULL,
    name character varying(45) NOT NULL,
    country_id bigint NOT NULL
);


ALTER TABLE "jcrm-dictionary-db".city OWNER TO postgres;

--
-- Name: city_id_seq; Type: SEQUENCE; Schema: jcrm-dictionary-db; Owner: postgres
--

CREATE SEQUENCE "jcrm-dictionary-db".city_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "jcrm-dictionary-db".city_id_seq OWNER TO postgres;

--
-- Name: city_id_seq; Type: SEQUENCE OWNED BY; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER SEQUENCE "jcrm-dictionary-db".city_id_seq OWNED BY "jcrm-dictionary-db".city.id;


--
-- Name: country; Type: TABLE; Schema: jcrm-dictionary-db; Owner: postgres
--

CREATE TABLE "jcrm-dictionary-db".country (
    id bigint NOT NULL,
    name character varying(45) NOT NULL,
    flag_code character varying(25) NOT NULL,
    phone_code character varying(10) NOT NULL
);


ALTER TABLE "jcrm-dictionary-db".country OWNER TO postgres;

--
-- Name: country_id_seq; Type: SEQUENCE; Schema: jcrm-dictionary-db; Owner: postgres
--

CREATE SEQUENCE "jcrm-dictionary-db".country_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "jcrm-dictionary-db".country_id_seq OWNER TO postgres;

--
-- Name: country_id_seq; Type: SEQUENCE OWNED BY; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER SEQUENCE "jcrm-dictionary-db".country_id_seq OWNED BY "jcrm-dictionary-db".country.id;


--
-- Name: industry; Type: TABLE; Schema: jcrm-dictionary-db; Owner: postgres
--

CREATE TABLE "jcrm-dictionary-db".industry (
    id bigint NOT NULL,
    name character varying(45) NOT NULL
);


ALTER TABLE "jcrm-dictionary-db".industry OWNER TO postgres;

--
-- Name: industry_id_seq; Type: SEQUENCE; Schema: jcrm-dictionary-db; Owner: postgres
--

CREATE SEQUENCE "jcrm-dictionary-db".industry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "jcrm-dictionary-db".industry_id_seq OWNER TO postgres;

--
-- Name: industry_id_seq; Type: SEQUENCE OWNED BY; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER SEQUENCE "jcrm-dictionary-db".industry_id_seq OWNED BY "jcrm-dictionary-db".industry.id;


--
-- Name: email_confirmation; Type: TABLE; Schema: jcrm-user-db; Owner: postgres
--

CREATE TABLE "jcrm-user-db".email_confirmation (
    id bigint NOT NULL,
    code character varying(15) NOT NULL,
    email character varying(100) NOT NULL,
    expiration timestamp without time zone NOT NULL
);


ALTER TABLE "jcrm-user-db".email_confirmation OWNER TO postgres;

--
-- Name: email_confirmation_id_seq; Type: SEQUENCE; Schema: jcrm-user-db; Owner: postgres
--

CREATE SEQUENCE "jcrm-user-db".email_confirmation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "jcrm-user-db".email_confirmation_id_seq OWNER TO postgres;

--
-- Name: email_confirmation_id_seq; Type: SEQUENCE OWNED BY; Schema: jcrm-user-db; Owner: postgres
--

ALTER SEQUENCE "jcrm-user-db".email_confirmation_id_seq OWNED BY "jcrm-user-db".email_confirmation.id;


--
-- Name: organization; Type: TABLE; Schema: jcrm-user-db; Owner: postgres
--

CREATE TABLE "jcrm-user-db".organization (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    country_id bigint NOT NULL,
    version integer DEFAULT 0 NOT NULL,
    zip_code character varying(50),
    address character varying(255),
    email character varying(50),
    phone_number character varying(50),
    city_id bigint,
    industries jsonb
);


ALTER TABLE "jcrm-user-db".organization OWNER TO postgres;

--
-- Name: organization_id_seq; Type: SEQUENCE; Schema: jcrm-user-db; Owner: postgres
--

CREATE SEQUENCE "jcrm-user-db".organization_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "jcrm-user-db".organization_id_seq OWNER TO postgres;

--
-- Name: organization_id_seq; Type: SEQUENCE OWNED BY; Schema: jcrm-user-db; Owner: postgres
--

ALTER SEQUENCE "jcrm-user-db".organization_id_seq OWNED BY "jcrm-user-db".organization.id;


--
-- Name: user; Type: TABLE; Schema: jcrm-user-db; Owner: postgres
--

CREATE TABLE "jcrm-user-db"."user" (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    password bytea NOT NULL,
    salt bytea NOT NULL,
    organization_id bigint,
    roles jsonb,
    version integer DEFAULT 0 NOT NULL,
    first_name character varying(45),
    second_name character varying(45),
    third_name character varying(45),
    phone_number character varying(45),
    groups jsonb,
    email_confirmed boolean DEFAULT false NOT NULL
);


ALTER TABLE "jcrm-user-db"."user" OWNER TO postgres;

--
-- Name: user_group; Type: TABLE; Schema: jcrm-user-db; Owner: postgres
--

CREATE TABLE "jcrm-user-db".user_group (
    id bigint NOT NULL,
    name character varying(45) NOT NULL,
    organization_id bigint NOT NULL,
    roles jsonb,
    version integer DEFAULT 0 NOT NULL
);


ALTER TABLE "jcrm-user-db".user_group OWNER TO postgres;

--
-- Name: user_group_id_seq; Type: SEQUENCE; Schema: jcrm-user-db; Owner: postgres
--

CREATE SEQUENCE "jcrm-user-db".user_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "jcrm-user-db".user_group_id_seq OWNER TO postgres;

--
-- Name: user_group_id_seq; Type: SEQUENCE OWNED BY; Schema: jcrm-user-db; Owner: postgres
--

ALTER SEQUENCE "jcrm-user-db".user_group_id_seq OWNED BY "jcrm-user-db".user_group.id;


--
-- Name: user_id_seq; Type: SEQUENCE; Schema: jcrm-user-db; Owner: postgres
--

CREATE SEQUENCE "jcrm-user-db".user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "jcrm-user-db".user_id_seq OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: jcrm-user-db; Owner: postgres
--

ALTER SEQUENCE "jcrm-user-db".user_id_seq OWNED BY "jcrm-user-db"."user".id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- Name: city id; Type: DEFAULT; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-dictionary-db".city ALTER COLUMN id SET DEFAULT nextval('"jcrm-dictionary-db".city_id_seq'::regclass);


--
-- Name: country id; Type: DEFAULT; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-dictionary-db".country ALTER COLUMN id SET DEFAULT nextval('"jcrm-dictionary-db".country_id_seq'::regclass);


--
-- Name: industry id; Type: DEFAULT; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-dictionary-db".industry ALTER COLUMN id SET DEFAULT nextval('"jcrm-dictionary-db".industry_id_seq'::regclass);


--
-- Name: email_confirmation id; Type: DEFAULT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db".email_confirmation ALTER COLUMN id SET DEFAULT nextval('"jcrm-user-db".email_confirmation_id_seq'::regclass);


--
-- Name: organization id; Type: DEFAULT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db".organization ALTER COLUMN id SET DEFAULT nextval('"jcrm-user-db".organization_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db"."user" ALTER COLUMN id SET DEFAULT nextval('"jcrm-user-db".user_id_seq'::regclass);


--
-- Name: user_group id; Type: DEFAULT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db".user_group ALTER COLUMN id SET DEFAULT nextval('"jcrm-user-db".user_group_id_seq'::regclass);


--
-- Name: city city_pkey; Type: CONSTRAINT; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-dictionary-db".city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id);


--
-- Name: country country_pkey; Type: CONSTRAINT; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-dictionary-db".country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- Name: industry industry_pkey; Type: CONSTRAINT; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-dictionary-db".industry
    ADD CONSTRAINT industry_pkey PRIMARY KEY (id);


--
-- Name: email_confirmation email_confirmation_pk; Type: CONSTRAINT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db".email_confirmation
    ADD CONSTRAINT email_confirmation_pk PRIMARY KEY (id);


--
-- Name: organization organization_pkey; Type: CONSTRAINT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db".organization
    ADD CONSTRAINT organization_pkey PRIMARY KEY (id);


--
-- Name: user_group user_group_pkey; Type: CONSTRAINT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db".user_group
    ADD CONSTRAINT user_group_pkey PRIMARY KEY (id);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db"."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: city_name_and_country_uindex; Type: INDEX; Schema: jcrm-dictionary-db; Owner: postgres
--

CREATE UNIQUE INDEX city_name_and_country_uindex ON "jcrm-dictionary-db".city USING btree (name, country_id);


--
-- Name: country__name_index; Type: INDEX; Schema: jcrm-dictionary-db; Owner: postgres
--

CREATE INDEX country__name_index ON "jcrm-dictionary-db".country USING btree (name);


--
-- Name: industry_name_uindex; Type: INDEX; Schema: jcrm-dictionary-db; Owner: postgres
--

CREATE UNIQUE INDEX industry_name_uindex ON "jcrm-dictionary-db".industry USING btree (name);


--
-- Name: email_confirmation_code_and_email_index; Type: INDEX; Schema: jcrm-user-db; Owner: postgres
--

CREATE INDEX email_confirmation_code_and_email_index ON "jcrm-user-db".email_confirmation USING btree (code, email);


--
-- Name: organization_name_uindex; Type: INDEX; Schema: jcrm-user-db; Owner: postgres
--

CREATE UNIQUE INDEX organization_name_uindex ON "jcrm-user-db".organization USING btree (name);


--
-- Name: user_group_name_and_org_uindex; Type: INDEX; Schema: jcrm-user-db; Owner: postgres
--

CREATE UNIQUE INDEX user_group_name_and_org_uindex ON "jcrm-user-db".user_group USING btree (name, organization_id);


--
-- Name: user_name_uindex; Type: INDEX; Schema: jcrm-user-db; Owner: postgres
--

CREATE UNIQUE INDEX user_name_uindex ON "jcrm-user-db"."user" USING btree (name);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: city city_to_country_fk; Type: FK CONSTRAINT; Schema: jcrm-dictionary-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-dictionary-db".city
    ADD CONSTRAINT city_to_country_fk FOREIGN KEY (country_id) REFERENCES "jcrm-dictionary-db".country(id);


--
-- Name: user_group user_group_to_org_fk; Type: FK CONSTRAINT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db".user_group
    ADD CONSTRAINT user_group_to_org_fk FOREIGN KEY (organization_id) REFERENCES "jcrm-user-db".organization(id);


--
-- Name: user user_to_org_fk; Type: FK CONSTRAINT; Schema: jcrm-user-db; Owner: postgres
--

ALTER TABLE ONLY "jcrm-user-db"."user"
    ADD CONSTRAINT user_to_org_fk FOREIGN KEY (organization_id) REFERENCES "jcrm-user-db".organization(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

