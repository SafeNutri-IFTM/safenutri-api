INSERT INTO public.receita (id, user_id, tipo_receita_id, titulo, descricao, tempo_preparo, porcao, calorias, 
imagem, public, activated, created, updated, ingredientes) 
VALUES
(1, 1, 2, 'Panqueca de Aveia e Banana', 'Café da manhã rico em potássio e fibras, sem açúcar refinado.', 
    20, 1, 240, '1.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 banana madura, 
    2 colheres de sopa de aveia em flocos, 
    1 ovo, 
    canela a gosto.'
),

(2, 1, 1, 'Quinoa com Legumes Assados', 'Refeição proteica completa e colorida, ideal para marmitas saudáveis.', 
    35, 2, 310, '2.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 xícara de quinoa cozida, 
    1 abobrinha, 
    1 cenoura, 
    1 pimentão, 
    1 colher de azeite de oliva.'
),

(3, 1, 5, 'Suco Verde Energizante', 'Bebida funcional para auxiliar na digestão e imunidade.', 
    5, 1, 85, '3.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '2 folhas de couve, 
    1 maçã, 
    suco de 1 limão, 
    200ml de água de coco.'
),

(4, 1, 1, 'Omelete de Espinafre e Ricota', 'Opção leve de jantar com alto teor de ferro.', 
    10, 1, 210, '4.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '3 ovos, 
    1 punhado de espinafre fresco, 
    2 colheres de ricota esfarelada.'
),

(5, 1, 1, 'Creme de Abóbora com Gengibre', 'Sopa termogênica e reconfortante para dias frios.', 
    30, 3, 160, '5.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '500g de abóbora cabotiá, 
    1 pedaço de gengibre, 
    1 cebola, 
    1 alho, 
    sal marinho.'
),

(6, 1, 5, 'Smoothie Antioxidante de Frutas Vermelhas', 'Bebida refrescante rica em flavonoides.', 
    5, 1, 190, '6.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 xícara de morangos e mirtilos, 
    150ml de leite de amêndoas, 
    mel opcional.'
),

(7, 1, 1, 'Frango com Crosta de Ervas', 'Peito de frango suculento com temperos naturais.', 
    25, 1, 280, '7.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '150g de peito de frango, 
    alecrim, 
    tomilho, 
    salsinha, 
    fio de azeite.'
),

(8, 1, 2, 'Pudim de Chia e Coco', 'Lanche da tarde que promove saciedade e saúde intestinal.', 
    10, 2, 220, '8.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '3 colheres de sopa de chia, 
    200ml de leite de coco caseiro, 
    adoçante estévia.'
),

(9, 1, 1, 'Guacamole Proteico', 'Gorduras boas do abacate com um toque de coentro.', 
    10, 4, 150, '9.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 abacate maduro, 
    tomate picado, 
    cebola roxa, 
    limão, 
    coentro, 
    sal.'
),

(10, 1, 2, 'Muffins de Banana e Cacau', 'Lanche doce fit para crianças e adultos.', 
    30, 6, 180, '10.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '3 bananas, 
    2 ovos, 
    1 xícara de aveia, 
    2 colheres de cacau 100%.'
),

(11, 1, 3, 'Salmão Grelhado com Manga', 'Prato sofisticado unindo proteínas e o doce da fruta.', 
    20, 1, 350, '11.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 filé de salmão, 
    cubos de manga fresca, 
    pimenta dedo-de-moça, 
    limão.'
),

(12, 1, 1, 'Espaguete de Abobrinha ao Pesto', 'Substituto leve para massas tradicionais, baixo em carboidratos.', 
    15, 1, 195, '12.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '2 abobrinhas cortadas em tiras finas, 
    manjericão, 
    castanhas, 
    azeite, 
    alho.'
),

(13, 1, 1, 'Crepioca de Queijo Branco', 'Clássico do lanche fit, prático e versátil.', 
    8, 1, 230, '13.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 ovo, 
    2 colheres de goma de tapioca, 
    1 fatia de queijo minas frescal.'
),

(14, 1, 4, 'Molho de Iogurte com Hortelã', 'Acompanhamento refrescante para saladas e grelhados.', 
    5, 5, 45, '14.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 pote de iogurte desnatado, 
    hortelã fresca, 
    sal, 
    limão.'
),

(15, 1, 1, 'Salada de Grão-de-Bico Mediterrânea', 'Fonte de fibras e proteínas vegetais.', 
    15, 2, 290, '15.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 xícara de grão-de-bico cozido, 
    pepino, 
    tomate cereja, 
    azeitonas, 
    azeite.'
),

(16, 1, 1, 'Arroz de Couve-Flor', 'Excelente opção low-carb para substituir o arroz branco.', 
    15, 2, 70, '16.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 couve-flor triturada, 
    cebola picada, 
    salsinha, 
    sal.'
),

(17, 1, 3, 'Espetinho de Frango com Abacaxi', 'Combinação agridoce grelhada deliciosa.', 
    20, 2, 260, '17.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '200g de frango em cubos, 
    fatias de abacaxi, 
    pimentão verde.'
),

(18, 1, 1, 'Chips de Batata Doce Assada', 'Snack saudável para substituir batata frita industrializada.', 
    40, 2, 140, '18.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '1 batata doce grande fatiada fina, 
    páprica defumada, 
    azeite.'
),

(19, 1, 4, 'Molho de Tomate Caseiro Rústico', 'Livre de conservantes e açúcares dos molhos prontos.', 
    45, 4, 60, '19.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '6 tomates maduros, 
    1 manjericão, 
    1 cebola, 
    1 alho, 
    azeite de oliva.'
),

(20, 1, 2, 'Sorvete de Banana "Nicecream"', 'Apenas um ingrediente para uma sobremesa gelada e saudável.', 
    5, 2, 110, '20.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 
    '3 bananas congeladas picadas.'
);

SELECT setval('public.id_seq_receita', (SELECT MAX(id) FROM public.receita));


INSERT INTO public.modo_preparo (id, receita_id, passo, titulo, descricao, created) 
VALUES
(1, 1, 1, 'Preparo da Massa', 'Amasse bem a banana com um garfo e misture o ovo e a aveia.', CURRENT_TIMESTAMP),
(2, 1, 2, 'Cozimento', 'Aqueça uma frigideira antiaderente e doure a panqueca dos dois lados.', CURRENT_TIMESTAMP),
(3, 2, 1, 'Legumes', 'Corte os legumes e asse com azeite por 25 minutos a 200 graus.', CURRENT_TIMESTAMP),
(4, 2, 2, 'Finalização', 'Misture a quinoa cozida com os legumes assados e sirva.', CURRENT_TIMESTAMP),
(5, 3, 1, 'Processamento', 'Bata todos os ingredientes no liquidificador até ficar homogêneo.', CURRENT_TIMESTAMP),
(6, 4, 1, 'Ovos', 'Bata os ovos e jogue na frigideira com o espinafre e a ricota.', CURRENT_TIMESTAMP),
(7, 5, 1, 'Cozimento', 'Cozinhe a abóbora com temperos até ficar macia.', CURRENT_TIMESTAMP),
(8, 5, 2, 'Creme', 'Bata tudo no liquidificador até obter uma textura aveludada.', CURRENT_TIMESTAMP),
(9, 12, 1, 'Corte', 'Corte a abobrinha em fios longos como se fosse espaguete.', CURRENT_TIMESTAMP),
(10, 12, 2, 'Saltear', 'Refogue rapidamente a abobrinha e misture o molho pesto.', CURRENT_TIMESTAMP),
(11, 20, 1, 'Bater', 'Bata as bananas congeladas no processador até virar creme.', CURRENT_TIMESTAMP);

SELECT setval('public.id_seq_modo_preparo', (SELECT MAX(id) FROM public.modo_preparo));


INSERT INTO public.receita_restricao (id, receita_id, restricao_id, created) 
VALUES
(1, 1, 1, CURRENT_TIMESTAMP), 
(2, 1, 8, CURRENT_TIMESTAMP), 
(3, 2, 7, CURRENT_TIMESTAMP), 
(4, 2, 2, CURRENT_TIMESTAMP), 
(5, 3, 7, CURRENT_TIMESTAMP), 
(6, 3, 9, CURRENT_TIMESTAMP), 
(7, 5, 7, CURRENT_TIMESTAMP), 
(8, 8, 7, CURRENT_TIMESTAMP), 
(9, 8, 9, CURRENT_TIMESTAMP), 
(10, 9, 7, CURRENT_TIMESTAMP), 
(11, 12, 7, CURRENT_TIMESTAMP), 
(12, 12, 2, CURRENT_TIMESTAMP), 
(13, 15, 7, CURRENT_TIMESTAMP), 
(14, 16, 7, CURRENT_TIMESTAMP), 
(15, 16, 9, CURRENT_TIMESTAMP), 
(16, 18, 7, CURRENT_TIMESTAMP), 
(17, 19, 7, CURRENT_TIMESTAMP), 
(18, 20, 7, CURRENT_TIMESTAMP), 
(19, 20, 1, CURRENT_TIMESTAMP); 

SELECT setval('public.id_seq_receita_restricao', (SELECT MAX(id) FROM public.receita_restricao));