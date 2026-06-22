-- Atualiza os status conforme a nova regra de negócio da Sprint 7
UPDATE public.status_denuncia SET status = 'CONCLUÍDO' WHERE id = 2;
UPDATE public.status_denuncia SET status = 'SUSPENSA' WHERE id = 4;