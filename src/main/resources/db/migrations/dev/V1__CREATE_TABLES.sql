--TABLE GENERO---------------------------------------------------------------------------------------------
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

--TABLE ROLE-----------------------------------------------------------------------------------------------
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

--TABLE RESTRICAO ALIMENTAR--------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_restricao_alimentar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.restricao_alimentar (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_restricao_alimentar'),
    restricao VARCHAR(255) NOT NULL,
    descricao TEXT,
    approved BOOLEAN,
    activated BOOLEAN,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP
);

ALTER TABLE public.restricao_alimentar
    ADD CONSTRAINT pk_restricao_alimentar PRIMARY KEY (id);

ALTER TABLE public.restricao_alimentar   
    ADD CONSTRAINT uk_restricao UNIQUE (restricao);

ALTER SEQUENCE public.id_seq_restricao_alimentar OWNER TO postgres;
ALTER SEQUENCE public.id_seq_restricao_alimentar OWNED BY public.restricao_alimentar.id;

--TABLE USER-----------------------------------------------------------------------------------------------
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
    activated BOOLEAN,
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

--TABLE USER RESTRICAO-------------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_user_restricao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.user_restricao (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_user_restricao'),
    user_id BIGINT NOT NULL,
    restricao_id BIGINT NOT NULL,
    activated BOOLEAN,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP
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

--TABLE NUTRICIONISTA--------------------------------------------------------------------------------------
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
    desc_perfil TEXT,
    caminho_foto VARCHAR(255),
    activated BOOLEAN,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP
);

ALTER TABLE public.nutricionista 
    ADD CONSTRAINT pk_nutricionista PRIMARY KEY (id);

ALTER TABLE public.nutricionista 
    ADD CONSTRAINT uk_nutricionista_crn UNIQUE (crn);

ALTER TABLE public.nutricionista 
    ADD CONSTRAINT uk_nutricionista_user UNIQUE (user_id);

ALTER TABLE public.nutricionista 
    ADD CONSTRAINT fk_nutricionista_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);

ALTER SEQUENCE public.id_seq_nutricionista OWNER TO postgres;
ALTER SEQUENCE public.id_seq_nutricionista OWNED BY public.nutricionista.id;

--TABLE TIPO RECEITA---------------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_tipo_receita
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.tipo_receita(
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_tipo_receita'),
    tipo_receita VARCHAR(255)
);

ALTER TABLE public.tipo_receita
    ADD CONSTRAINT pk_tipo_receita PRIMARY KEY (id);

ALTER SEQUENCE public.id_seq_tipo_receita OWNER TO postgres;
ALTER SEQUENCE public.id_seq_tipo_receita OWNED BY public.tipo_receita.id;

--TABLE RECEITA--------------------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_receita
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.receita(
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_receita'),
    user_id BIGINT NOT NULL,
    tipo_receita_id BIGINT NOT NULL,
    titulo VARCHAR(255),
    descricao TEXT,
    tempo_preparo INTEGER,
    porcao INTEGER,
    calorias INTEGER,
    caminho_foto VARCHAR(255),
    public BOOLEAN,
    activated BOOLEAN,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP
);

ALTER TABLE public.receita
    ADD CONSTRAINT pk_receita PRIMARY KEY (id);

ALTER TABLE public.receita
    ADD CONSTRAINT fk_receita_tipo_receita FOREIGN KEY (tipo_receita_id)
    REFERENCES public.tipo_receita (id);

ALTER SEQUENCE public.id_seq_receita OWNER TO postgres;
ALTER SEQUENCE public.id_seq_receita OWNED BY public.receita.id;

--TABLE RECEITA RESTRICAO----------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_receita_restricao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.receita_restricao (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_receita_restricao'),
    receita_id BIGINT NOT NULL,
    restricao_id BIGINT NOT NULL,
    activated BOOLEAN,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP
);

ALTER TABLE public.receita_restricao
    ADD CONSTRAINT pk_receita_restricao PRIMARY KEY (id);

ALTER TABLE public.receita_restricao    
    ADD CONSTRAINT uk_receita_restricao UNIQUE (receita_id, restricao_id);

ALTER TABLE public.receita_restricao
    ADD CONSTRAINT fk_receita_restricao_receita FOREIGN KEY (receita_id)
    REFERENCES public.receita (id);

ALTER TABLE public.receita_restricao
    ADD CONSTRAINT fk_receita_restricao_restricao_alimentar FOREIGN KEY (restricao_id)
    REFERENCES public.restricao_alimentar (id);

ALTER SEQUENCE public.id_seq_receita_restricao OWNER TO postgres;
ALTER SEQUENCE public.id_seq_receita_restricao OWNED BY public.receita_restricao.id;

--TABLE INGREDIENTE----------------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_ingrediente
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.ingrediente (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_ingrediente'),
    ingrediente VARCHAR(255)
);

ALTER TABLE public.ingrediente
    ADD CONSTRAINT pk_ingrediente PRIMARY KEY (id);

ALTER SEQUENCE public.id_seq_ingrediente OWNER TO postgres;
ALTER SEQUENCE public.id_seq_ingrediente OWNED BY public.ingrediente.id;

--TABLE RECEITA INGREDIENTE--------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_receita_ingrediente
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.receita_ingrediente (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_receita_ingrediente'), 
    receita_id BIGINT NOT NULL,
    ingredinte_id BIGINT NOT NULL,
    quantidade INTEGER,
    unidade VARCHAR(10)
);

ALTER TABLE public.receita_ingrediente
    ADD CONSTRAINT pk_receita_ingrediente PRIMARY KEY (id);

ALTER TABLE public.receita_ingrediente
    ADD CONSTRAINT fk_receita_ingrediente_receita FOREIGN KEY (receita_id)
    REFERENCES public.receita (id);

ALTER TABLE public.receita_ingrediente
    ADD CONSTRAINT fk_receita_ingrediente_ingrediente FOREIGN KEY (ingredinte_id)
    REFERENCES public.ingrediente (id);

ALTER TABLE public.receita_ingrediente    
    ADD CONSTRAINT uk_receita_ingrediente UNIQUE (receita_id, ingredinte_id);

ALTER SEQUENCE public.id_seq_receita_ingrediente OWNER TO postgres;
ALTER SEQUENCE public.id_seq_receita_ingrediente OWNED BY public.receita_ingrediente.id;

--TABLE MODO PREPARO---------------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_modo_preparo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.modo_preparo (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_modo_preparo'), 
    receita_id BIGINT NOT NULL,
    passo INTEGER,
    titulo VARCHAR(255),
    descricao TEXT,
    activated BOOLEAN,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP
);

ALTER TABLE public.modo_preparo
    ADD CONSTRAINT pk_modo_preparo PRIMARY KEY (id);

ALTER TABLE public.modo_preparo
    ADD CONSTRAINT fk_modo_preparo_receita FOREIGN KEY (receita_id)
    REFERENCES public.receita (id);

ALTER TABLE public.modo_preparo    
    ADD CONSTRAINT uk_modo_preparo_receita UNIQUE (receita_id, passo);

ALTER SEQUENCE public.id_seq_modo_preparo OWNER TO postgres;
ALTER SEQUENCE public.id_seq_modo_preparo OWNED BY public.modo_preparo.id;