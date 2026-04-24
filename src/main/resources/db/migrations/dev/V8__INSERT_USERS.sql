INSERT INTO public.user (id, genero_id, role_id, nome, email, senha, dt_nascimento, activated, created, updated)
VALUES 
(1, 1, 2, 'Isaac Araujo', 'isaac@gmail.com', '$2a$10$mrIKbki4FVwyeRgP/6XBeuutVAPi6OFRcdpYzlLwPDyMshy/DRBeC', '2000-01-01', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 2, 'Thiago Pimenta', 'thiago@gmail.com', '$2a$10$mrIKbki4FVwyeRgP/6XBeuutVAPi6OFRcdpYzlLwPDyMshy/DRBeC', '2000-01-01', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, 3, 'Yuri Oliveira', 'yuri@gmail.com', '$2a$10$mrIKbki4FVwyeRgP/6XBeuutVAPi6OFRcdpYzlLwPDyMshy/DRBeC', '2000-01-01', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 1, 3, 'Alberto Domingos', 'alberto@gmail.com', '$2a$10$mrIKbki4FVwyeRgP/6XBeuutVAPi6OFRcdpYzlLwPDyMshy/DRBeC', '2000-01-01', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('public.id_seq_user', (SELECT MAX(id) FROM public.user));

INSERT INTO public.nutricionista (id, user_id, crn, activated, created, updated)
VALUES 
(1, 1, 'CRN1/123', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 'CRN1/456', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('public.id_seq_nutricionista', (SELECT MAX(id) FROM public.nutricionista));

INSERT INTO public.paciente (id, user_id, nutricionista_id, activated, created, updated)
VALUES
(1, 3, 2, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 4, 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 4, 2, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('public.id_seq_paciente', (SELECT MAX(id) FROM public.paciente));