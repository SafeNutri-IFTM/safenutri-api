ALTER TABLE public.nutricionista 
ADD COLUMN token_convite UUID NOT NULL DEFAULT gen_random_uuid();

ALTER TABLE public.nutricionista 
ADD CONSTRAINT uk_nutricionista_token UNIQUE (token_convite);