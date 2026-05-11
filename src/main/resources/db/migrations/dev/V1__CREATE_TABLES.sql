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
    activated BOOLEAN NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    imagem_perfil VARCHAR(255),
    activated BOOLEAN NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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

--TABLE RESTRICAO ALIMENTAR--------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_restricao_alimentar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.restricao_alimentar (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_restricao_alimentar'),
    nutricionista_id BIGINT NULL,
    restricao VARCHAR(255) NOT NULL,
    descricao TEXT,
    activated BOOLEAN NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE public.restricao_alimentar
    ADD CONSTRAINT pk_restricao_alimentar PRIMARY KEY (id);

ALTER TABLE public.restricao_alimentar   
    ADD CONSTRAINT uk_restricao UNIQUE (restricao);

ALTER TABLE public.restricao_alimentar
    ADD CONSTRAINT fk_restricao_alimentar_nutricionista FOREIGN KEY (nutricionista_id)
    REFERENCES public.nutricionista (user_id);

ALTER SEQUENCE public.id_seq_restricao_alimentar OWNER TO postgres;
ALTER SEQUENCE public.id_seq_restricao_alimentar OWNED BY public.restricao_alimentar.id;

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
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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

--TABLE TIPO RECEITA---------------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_tipo_receita
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.tipo_receita(
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_tipo_receita'),
    tipo_receita VARCHAR(255) NOT NULL
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
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    ingredientes TEXT,
    tempo_preparo INTEGER NOT NULL,
    porcao INTEGER NOT NULL,
    calorias INTEGER NOT NULL,
    imagem VARCHAR(255),
    public BOOLEAN NOT NULL,
    activated BOOLEAN NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    passo INTEGER NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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

--TABLE DOCUMENTOS LEGAIS----------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_documentos_legais
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.documentos_legais (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_documentos_legais'),
    tipo VARCHAR(100) NOT NULL,
    versao VARCHAR(50) NOT NULL,
    texto TEXT NOT NULL,
    activated BOOLEAN NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE public.documentos_legais
    ADD CONSTRAINT pk_documentos_legais PRIMARY KEY (id);

ALTER TABLE public.documentos_legais    
    ADD CONSTRAINT uk_documentos_legais_tipo_versao UNIQUE (tipo, versao);

ALTER SEQUENCE public.id_seq_documentos_legais OWNER TO postgres;
ALTER SEQUENCE public.id_seq_documentos_legais OWNED BY public.documentos_legais.id;

--TABLE ACEITO TERMOS--------------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_aceito_termos
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.aceito_termos (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_aceito_termos'),
    user_id BIGINT NOT NULL,
    documento_id BIGINT NOT NULL,
    data_aceito_termos TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE public.aceito_termos
    ADD CONSTRAINT pk_aceito_termos PRIMARY KEY (id);

ALTER TABLE public.aceito_termos
    ADD CONSTRAINT fk_aceito_termos_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);

ALTER TABLE public.aceito_termos
    ADD CONSTRAINT fk_aceito_termos_documentos FOREIGN KEY (documento_id)
    REFERENCES public.documentos_legais (id);

ALTER TABLE public.aceito_termos    
    ADD CONSTRAINT uk_aceito_termos_user_doc UNIQUE (user_id, documento_id);

ALTER SEQUENCE public.id_seq_aceito_termos OWNER TO postgres;
ALTER SEQUENCE public.id_seq_aceito_termos OWNED BY public.aceito_termos.id;

--TABLE RECEITA_AVALIAÇÃO ---------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_receita_avaliacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.receita_avaliacao (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_receita_avaliacao'),
    user_id BIGINT NOT NULL,
    receita_id BIGINT NOT NULL,
    qntd_estrela INT,
    descricao TEXT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP
);

ALTER TABLE public.receita_avaliacao 
    ADD CONSTRAINT pk_receita_avaliacao PRIMARY KEY (id);

ALTER TABLE public.receita_avaliacao    
    ADD CONSTRAINT uk_receita_avaliacao UNIQUE (user_id, receita_id);

ALTER TABLE public.receita_avaliacao 
    ADD CONSTRAINT fk_receita_avaliacao_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);

ALTER TABLE public.receita_avaliacao
    ADD CONSTRAINT fk_receita_avaliacao_receita FOREIGN KEY (receita_id)
    REFERENCES public.receita (id);

ALTER SEQUENCE public.id_seq_receita_avaliacao OWNER TO postgres;
ALTER SEQUENCE public.id_seq_receita_avaliacao OWNED BY public.receita_avaliacao.id;

--TABLE RECEITA_FAVORITA ----------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_receita_favorita
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.receita_favorita (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_receita_favorita'),
    user_id BIGINT NOT NULL,
    receita_id BIGINT NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE public.receita_favorita 
    ADD CONSTRAINT pk_receita_favorita PRIMARY KEY (id);

ALTER TABLE public.receita_favorita    
    ADD CONSTRAINT uk_receita_favorita UNIQUE (user_id, receita_id);

ALTER TABLE public.receita_favorita
    ADD CONSTRAINT fk_receita_favorita_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);

ALTER TABLE public.receita_favorita
    ADD CONSTRAINT fk_receita_favorita_receita FOREIGN KEY (receita_id)
    REFERENCES public.receita (id);

ALTER SEQUENCE public.id_seq_receita_favorita OWNER TO postgres;
ALTER SEQUENCE public.id_seq_receita_favorita OWNED BY public.receita_favorita.id;

--TABLE PACIENTE ------------------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_paciente
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.paciente (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_paciente'),
    user_id BIGINT NOT NULL,
    nutricionista_id BIGINT NOT NULL,
    activated BOOLEAN,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP
);

ALTER TABLE public.paciente 
    ADD CONSTRAINT pk_paciente PRIMARY KEY (id);

ALTER TABLE public.paciente    
    ADD CONSTRAINT uk_paciente UNIQUE (user_id, nutricionista_id);

ALTER TABLE public.paciente 
    ADD CONSTRAINT fk_paciente_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);

ALTER TABLE public.paciente
    ADD CONSTRAINT fk_paciente_nutricionista FOREIGN KEY (nutricionista_id)
    REFERENCES public.nutricionista (user_id);

ALTER SEQUENCE public.id_seq_paciente OWNER TO postgres;
ALTER SEQUENCE public.id_seq_paciente OWNED BY public.paciente.id;

--TABLE CUSTOM RESTRICAO-------------------------------------------------------------------------------------
CREATE SEQUENCE public.id_seq_custom_restricao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.custom_restricao (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_custom_restricao'),
    user_id BIGINT NOT NULL,
    restricao VARCHAR(255) NOT NULL,
    descricao TEXT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE public.custom_restricao
    ADD CONSTRAINT pk_custom_restricao PRIMARY KEY (id);

ALTER TABLE public.custom_restricao
    ADD CONSTRAINT fk_custom_restricao_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);
