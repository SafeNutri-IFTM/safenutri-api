ALTER TABLE public.receita 
ADD COLUMN ingredientes TEXT;

DROP TABLE public.receita_ingrediente;

DROP TABLE public.ingrediente;