SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;
SET search_path = public, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;

ALTER SCHEMA public OWNER TO test;

CREATE SEQUENCE privilege_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE privileges (
    id bigint NOT NULL,
    description character varying(255),
    domainid character varying(255),
    name character varying(255) NOT NULL,
    version bigint
);

CREATE SEQUENCE role_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE roles (
    id bigint NOT NULL,
    description character varying(255),
    domainid character varying(255),
    name character varying(255) NOT NULL,
    version bigint
);

CREATE TABLE roles_privileges (
    role_id bigint NOT NULL,
    privileges_id bigint NOT NULL
);

CREATE SEQUENCE user_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE users (
    id bigint NOT NULL,
    displayname character varying(255),
    domainid character varying(255),
    name character varying(255) NOT NULL,
    version bigint
);

CREATE TABLE users_roles (
    user_id bigint NOT NULL,
    roles_id bigint NOT NULL
);

ALTER TABLE ONLY privileges
    ADD CONSTRAINT privileges_pkey PRIMARY KEY (id);

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_name_unique UNIQUE (name);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_displayname_unique UNIQUE (displayname);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_domainid_unique UNIQUE (domainid);

ALTER TABLE ONLY privileges
    ADD CONSTRAINT privileges_domainid_unique UNIQUE (domainid);

ALTER TABLE ONLY privileges
    ADD CONSTRAINT privileges_name_unique UNIQUE (name);

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_name_unique UNIQUE (name);

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_domainid_unique UNIQUE (domainid);

ALTER TABLE ONLY users_roles
    ADD CONSTRAINT users_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ONLY roles_privileges
    ADD CONSTRAINT roles_privileges_role_id_fkey FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE ONLY users_roles
    ADD CONSTRAINT users_roles_roles_id_fkey FOREIGN KEY (roles_id) REFERENCES roles(id);

ALTER TABLE ONLY roles_privileges
    ADD CONSTRAINT roles_privileges_privileges_id_fkey FOREIGN KEY (privileges_id) REFERENCES privileges(id);
