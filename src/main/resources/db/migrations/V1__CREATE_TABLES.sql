--TABLE GENERO
CREATE SEQUENCE public.id_seq_genero
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.genero (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_genero'),
    genero VARCHAR(100) NOT NULL
);

ALTER TABLE public.genero
    ADD CONSTRAINT pk_genero PRIMARY KEY (id);

ALTER TABLE public.genero    
    ADD CONSTRAINT uk_genero UNIQUE (genero);

ALTER SEQUENCE public.id_seq_genero OWNER TO postgres;
ALTER SEQUENCE public.id_seq_genero OWNED BY public.genero.id;

--TABLE ROLE
CREATE SEQUENCE public.id_seq_role
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.role (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_role'),
    role VARCHAR(100) NOT NULL
);

ALTER TABLE public.role
    ADD CONSTRAINT pk_role PRIMARY KEY (id);

ALTER TABLE public.role    
    ADD CONSTRAINT uk_role UNIQUE (role);

ALTER SEQUENCE public.id_seq_role OWNER TO postgres;
ALTER SEQUENCE public.id_seq_role OWNED BY public.role.id;

--TABLE RESTRICAO ALIMENTAR
CREATE SEQUENCE public.id_seq_restricao_alimentar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.restricao_alimentar (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_restricao_alimentar'),
    restricao VARCHAR(255) NOT NULL
);

ALTER TABLE public.restricao_alimentar
    ADD CONSTRAINT pk_restricao_alimentar PRIMARY KEY (id);

ALTER SEQUENCE public.id_seq_restricao_alimentar OWNER TO postgres;
ALTER SEQUENCE public.id_seq_restricao_alimentar OWNED BY public.restricao_alimentar.id;

--TABLE USER
CREATE SEQUENCE public.id_seq_user
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.user (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_user'),
    genero_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(50) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    dt_nascimento DATE NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP
);

ALTER TABLE public.user
    ADD CONSTRAINT pk_user PRIMARY KEY (id);

ALTER TABLE public.user  
    ADD CONSTRAINT fk_user_genero FOREIGN KEY (genero_id)
    REFERENCES public.genero (id);

ALTER TABLE public.user    
    ADD CONSTRAINT fk_user_role FOREIGN KEY (role_id)
    REFERENCES public.role (id);

ALTER TABLE public.user   
    ADD CONSTRAINT uk_user_email UNIQUE (email);

ALTER SEQUENCE public.id_seq_user OWNER TO postgres;
ALTER SEQUENCE public.id_seq_user OWNED BY public.user.id;

--TABLE USER RESTRICAO
CREATE SEQUENCE public.id_seq_user_restricao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.user_restricao (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_user_restricao'),
    user_id BIGINT NOT NULL,
    restricao_id BIGINT NOT NULL
);

ALTER TABLE public.user_restricao
    ADD CONSTRAINT pk_user_restricao PRIMARY KEY (id);

ALTER TABLE public.user_restricao    
    ADD CONSTRAINT uk_user_restricao UNIQUE (user_id, restricao_id);

ALTER TABLE public.user_restricao
    ADD CONSTRAINT fk_user_restricao_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);

ALTER TABLE public.user_restricao
    ADD CONSTRAINT fk_user_restricao_restricao_alimentar FOREIGN KEY (restricao_id)
    REFERENCES public.restricao_alimentar (id);

ALTER SEQUENCE public.id_seq_user_restricao OWNER TO postgres;
ALTER SEQUENCE public.id_seq_user_restricao OWNED BY public.user_restricao.id;

--TABLE NUTRICIONISTA
CREATE SEQUENCE public.id_seq_nutricionista
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.nutricionista (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_nutricionista'),
    user_id BIGINT NOT NULL,
    crn VARCHAR(100) NOT NULL,
    desc_perfil VARCHAR(255),
    caminho_foto VARCHAR(255)
);

ALTER TABLE public.nutricionista 
    ADD CONSTRAINT pk_nutricionista PRIMARY KEY (id);

ALTER TABLE public.nutricionista 
    ADD CONSTRAINT uk_nutricionista_user UNIQUE (user_id);

ALTER TABLE public.nutricionista 
    ADD CONSTRAINT fk_nutricionista_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);

ALTER SEQUENCE public.id_seq_nutricionista OWNER TO postgres;
ALTER SEQUENCE public.id_seq_nutricionista OWNED BY public.nutricionista.id;