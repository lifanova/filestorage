CREATE ROLE file_user WITH
    LOGIN
    NOSUPERUSER
    INHERIT
    CREATEDB
    NOCREATEROLE
    NOREPLICATION
    ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:mq92f4J41TcWjWAyu65JmQ==$rpQzC2yjusKlcvhM1nw9JbT4CosxIxDP3zg/mTxA+X0=:/YdgSvjSihJQWMnrBUV7+zjTTPLQ0eKKcZYnsbnvZF4=';

CREATE TABLE IF NOT EXISTS public.file_entity
(
    id bigint NOT NULL DEFAULT nextval('file_entity_id_seq'::regclass),
    bytes bytea NOT NULL,
    created timestamp without time zone NOT NULL,
    mimetype character varying(255) COLLATE pg_catalog."default" NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    size bigint NOT NULL,
    updated timestamp without time zone NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT file_entity_pkey PRIMARY KEY (id),
    CONSTRAINT uk_98qasr758yqbungy6mj1bsxtt UNIQUE (name),
    CONSTRAINT fkm04nsoi97gotvf2b1q307tncv FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
                      ON UPDATE NO ACTION
                      ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.file_entity
    OWNER to file_user;

-- Table: public.users

-- DROP TABLE IF EXISTS public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    login character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk_ow0gan20590jrb00upg3va2fn UNIQUE (login),
    CONSTRAINT fk4qu1gr772nnf6ve5af002rwya FOREIGN KEY (role_id)
    REFERENCES public.role (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to file_user;

CREATE TABLE IF NOT EXISTS public.role
(
    id bigint NOT NULL DEFAULT nextval('role_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT role_pkey PRIMARY KEY (id),
    CONSTRAINT uk_8sewwnpamngi6b1dwaa88askk UNIQUE (name)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.role
    OWNER to file_user;