ALTER TABLE public.receita 
ADD COLUMN ingredientes TEXT;

DROP TABLE public.receita_ingrediente;

DROP TABLE public.ingrediente;

ALTER TABLE public.modo_preparo
DROP COLUMN activated, 
DROP COLUMN updated;

ALTER TABLE public.receita_restricao
DROP COLUMN activated, 
DROP COLUMN updated;