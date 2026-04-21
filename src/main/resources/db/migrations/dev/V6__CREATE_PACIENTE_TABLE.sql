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
    ADD CONSTRAINT fk_paciente_user FOREIGN KEY (user_id)
    REFERENCES public.user (id);

ALTER TABLE public.paciente
    ADD CONSTRAINT fk_paciente_nutricionista FOREIGN KEY (nutricionista_id)
    REFERENCES public.nutricionista (user_id);

ALTER SEQUENCE public.id_seq_paciente OWNER TO postgres;
ALTER SEQUENCE public.id_seq_paciente OWNED BY public.paciente.id;