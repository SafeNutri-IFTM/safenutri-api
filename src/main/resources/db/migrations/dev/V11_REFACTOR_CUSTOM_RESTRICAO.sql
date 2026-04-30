tirar as colunas public e activated da tabela custmo_restricao
ALTER TABLE public.custmo_restricao
DROP column public,
DROP column activated;