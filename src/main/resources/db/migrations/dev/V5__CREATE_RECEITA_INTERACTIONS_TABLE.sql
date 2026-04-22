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