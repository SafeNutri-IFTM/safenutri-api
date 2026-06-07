-- Inserindo os novos status de denúncia necessários para o fluxo de inativação
INSERT INTO public.status_denuncia (id, status)
VALUES
    (3, 'EM ANÁLISE'),
    (4, 'EM CORREÇÃO');

-----------------------------------------------------------------------------------------------------------
SELECT setval('public.id_seq_status_denuncia', (SELECT MAX(id) FROM public.status_denuncia));