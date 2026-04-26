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
    public BOOLEAN NOT NULL,
    activated BOOLEAN NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE public.custom_restricao
    ADD CONSTRAINT pk_custom_restricao PRIMARY KEY (id);

ALTER TABLE public.custom_restricao
    ADD CONSTRAINT fk_custom_restricao_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);
