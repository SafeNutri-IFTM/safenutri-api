ALTER TABLE public.restricao_alimentar
DROP COLUMN approved;

UPDATE public.restricao_alimentar
SET restricao = 'INTOLERÂNCIA À LACTOSE',
    descricao = 'Condição em que o organismo tem dificuldade ou incapacidade de digerir a lactose, 
        açúcar presente no leite e derivados, podendo causar sintomas como desconforto abdominal, 
        gases e diarreia.'
WHERE id = 1;

UPDATE public.restricao_alimentar
SET restricao = 'SENSIBILIDADE AO GLÚTEN',
    descricao = 'Condição em que o consumo de glúten causa desconforto gastrointestinal, sem envolver
    resposta autoimune ou dano permanente ao intestino, com sintomas que variam de acordo com a 
    sensibilidade individual.'
WHERE id = 2;

INSERT INTO public.restricao_alimentar (restricao, descricao, activated) 
VALUES ('ALERGIA AO LEITE', 
    'Reação imunológica às proteínas do leite, podendo causar sintomas que variam de leves a graves, 
    incluindo risco de anafilaxia, mesmo com pequenas quantidades.', 
    TRUE    
);

INSERT INTO public.restricao_alimentar (restricao, descricao, activated)
VALUES ('ALERGIA AO TRIGO',
    'Reação alérgica às proteínas do trigo, podendo causar sintomas imediatos como urticária, 
    dificuldade respiratória e, em casos graves, anafilaxia. Diferente da doença celíaca, não envolve
    reação autoimune ao glúten.',
    TRUE
);

INSERT INTO public.restricao_alimentar (restricao, descricao, activated)
VALUES ('DOENÇA CELÍACA',
    'Doença autoimune em que a ingestão de glúten (presente no trigo, cevada e centeio) provoca danos 
    ao intestino delgado, exigindo exclusão total e permanente do glúten.',
    TRUE
);

INSERT INTO public.restricao_alimentar (restricao, descricao, activated) 
VALUES ('INTOLERÂNCIA À FRUTOSE', 
    'Condição em que há dificuldade na absorção da frutose, levando a sintomas gastrointestinais 
    como inchaço, gases e desconforto abdominal.', 
    TRUE
);

INSERT INTO public.restricao_alimentar (restricao, descricao, activated) 
VALUES ('VEGANISMO', 
    'Padrão alimentar que exclui todos os produtos de origem animal, incluindo carnes, 
    leite, ovos, mel e quaisquer derivados.', 
    TRUE
);

INSERT INTO public.restricao_alimentar (restricao, descricao, activated) 
VALUES ('VEGETARIANISMO', 
    'Padrão alimentar que exclui o consumo de carnes (bovina, suína, aves e peixes), 
    podendo ou não incluir componentes de origem animal como leite e ovos, dependendo da variação.',
    TRUE
);

INSERT INTO public.restricao_alimentar (restricao, descricao, activated) 
VALUES ('DIABETES', 
    'Condição metabólica caracterizada pela dificuldade no controle da glicose no sangue, 
    exigindo atenção ao consumo de açúcares e carboidratos, especialmente os de rápida absorção..', 
    TRUE
);
