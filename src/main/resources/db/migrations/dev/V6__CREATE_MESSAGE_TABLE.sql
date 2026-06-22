CREATE SEQUENCE public.id_seq_mensagem
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.mensagem (
    id BIGINT NOT NULL DEFAULT nextval('public.id_seq_mensagem'),
    remetente_id BIGINT NOT NULL,
    destinatario_id BIGINT NOT NULL,
    conteudo TEXT NOT NULL,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    lida BOOLEAN DEFAULT FALSE
);