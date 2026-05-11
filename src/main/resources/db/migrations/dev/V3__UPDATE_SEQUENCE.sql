SELECT setval('public.id_seq_user', (SELECT MAX(id) FROM public.user));
SELECT setval('public.id_seq_user_restricao', (SELECT MAX(id) FROM public.user_restricao));
SELECT setval('public.id_seq_nutricionista', (SELECT MAX(id) FROM public.nutricionista));
SELECT setval('public.id_seq_paciente', (SELECT MAX(id) FROM public.paciente));
SELECT setval('public.id_seq_receita', (SELECT MAX(id) FROM public.receita));
SELECT setval('public.id_seq_modo_preparo', (SELECT MAX(id) FROM public.modo_preparo));
SELECT setval('public.id_seq_receita_restricao', (SELECT MAX(id) FROM public.receita_restricao));