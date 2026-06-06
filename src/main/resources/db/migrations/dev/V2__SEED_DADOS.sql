INSERT INTO public.genero (genero)
VALUES
    ('MASCULINO'),
    ('FEMININO');

-----------------------------------------------------------------------------------------------------------
INSERT INTO public.role (role)
VALUES
    ('ADMIN'),
    ('NUTRICIONISTA'),
    ('USER');

-----------------------------------------------------------------------------------------------------------
INSERT INTO public.restricao_alimentar (restricao, descricao, activated) 
VALUES
    ('INTOLERÂNCIA À LACTOSE',
     'Condição em que o organismo tem dificuldade ou incapacidade de digerir a lactose, açúcar presente no leite e derivados, podendo causar sintomas como desconforto abdominal, gases e diarreia.',
     TRUE
    ),

    ('SENSIBILIDADE AO GLÚTEN',
    'Condição em que o consumo de glúten causa desconforto gastrointestinal, sem envolver resposta autoimune ou dano permanente ao intestino, com sintomas que variam de acordo com a sensibilidade individual.',
    TRUE),

    ('ALERGIA AO LEITE',
    'Reação imunológica às proteínas do leite, podendo causar sintomas que variam de leves a graves, incluindo risco de anafilaxia, mesmo com pequenas quantidades.',
    TRUE),

    ('ALERGIA AO TRIGO',
    'Reação alérgica às proteínas do trigo, podendo causar sintomas imediatos como urticária, dificuldade respiratória e, em casos graves, anafilaxia. Diferente da doença celíaca, não envolve reação autoimune ao glúten.',
    TRUE),

    ('DOENÇA CELÍACA',
    'Doença autoimune em que a ingestão de glúten (presente no trigo, cevada e centeio) provoca danos ao intestino delgado, exigindo exclusão total e permanente do glúten.',
    TRUE),

    ('INTOLERÂNCIA À FRUTOSE',
    'Condição em que há dificuldade na absorção da frutose, levando a sintomas gastrointestinais como inchaço, gases e desconforto abdominal.',
    TRUE),

    ('VEGANISMO',
    'Padrão alimentar que exclui todos os produtos de origem animal, incluindo carnes, leite, ovos, mel e quaisquer derivados.',
    TRUE),

    ('VEGETARIANISMO',
    'Padrão alimentar que exclui o consumo de carnes (bovina, suína, aves e peixes), podendo ou não incluir componentes de origem animal como leite e ovos, dependendo da variação.',
    TRUE),

    ('DIABETES',
    'Condição metabólica caracterizada pela dificuldade no controle da glicose no sangue, exigindo atenção ao consumo de açúcares e carboidratos, especialmente os de rápida absorção..',
    TRUE);

-----------------------------------------------------------------------------------------------------------
INSERT INTO public.tipo_receita (tipo_receita)
VALUES
    ('SALGADA'),
    ('DOCE'),
    ('AGRIDOCE'),
    ('MOLHO'),
    ('BEBIDA');

-----------------------------------------------------------------------------------------------------------
INSERT INTO public.user (id, genero_id, role_id, nome, email, senha, dt_nascimento, activated, created, updated)
VALUES
    (1, 1, 2, 'Isaac Araujo', 'isaac@gmail.com', '$2a$10$mrIKbki4FVwyeRgP/6XBeuutVAPi6OFRcdpYzlLwPDyMshy/DRBeC', '2000-01-01', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1, 2, 'Thiago Pimenta', 'thiago@gmail.com', '$2a$10$mrIKbki4FVwyeRgP/6XBeuutVAPi6OFRcdpYzlLwPDyMshy/DRBeC', '2000-01-01', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 1, 3, 'Yuri Oliveira', 'yuri@gmail.com', '$2a$10$mrIKbki4FVwyeRgP/6XBeuutVAPi6OFRcdpYzlLwPDyMshy/DRBeC', '2000-01-01', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 1, 3, 'Alberto Domingos', 'alberto@gmail.com', '$2a$10$mrIKbki4FVwyeRgP/6XBeuutVAPi6OFRcdpYzlLwPDyMshy/DRBeC', '2000-01-01', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 1, 1, 'Root', 'root@safenutri.com.br', '$2a$10$mrIKbki4FVwyeRgP/6XBeuutVAPi6OFRcdpYzlLwPDyMshy/DRBeC', '2000-01-01', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-----------------------------------------------------------------------------------------------------------
INSERT INTO public.user_restricao(id, user_id, restricao_id, created)
VALUES
    (1, 3, 7, CURRENT_TIMESTAMP);

-----------------------------------------------------------------------------------------------------------
INSERT INTO public.nutricionista (id, user_id, crn, activated, created, updated)
VALUES
    (1, 1, 'CRN1/123', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, 'CRN1/456', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-----------------------------------------------------------------------------------------------------------
INSERT INTO public.paciente (id, user_id, nutricionista_id, activated, created, updated)
VALUES
    (1, 3, 2, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 4, 1, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-----------------------------------------------------------------------------------------------------------
INSERT INTO public.receita (id, user_id, tipo_receita_id, titulo, descricao, tempo_preparo, porcao, calorias, 
imagem, public, activated, created, updated, ingredientes) 
VALUES
    (1, 1, 2, 'Panqueca de Aveia e Banana', 'Café da manhã rico em potássio e fibras, sem açúcar refinado.',
    20, 1, 240, '1.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 banana madura,
2 colheres de sopa de aveia em flocos, 
1 ovo, 
canela a gosto.'),

    (2, 1, 1, 'Quinoa com Legumes Assados', 'Refeição proteica completa e colorida, ideal para marmitas saudáveis.',
    35, 2, 310, '2.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 xícara de quinoa cozida,
1 abobrinha, 
1 cenoura, 
1 pimentão, 
1 colher de azeite de oliva.'),

    (3, 1, 5, 'Suco Verde Energizante', 'Bebida funcional para auxiliar na digestão e imunidade.',
    5, 1, 85, '3.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '2 folhas de couve,
1 maçã, 
suco de 1 limão, 
200ml de água de coco.'),

    (4, 1, 1, 'Omelete de Espinafre e Ricota', 'Opção leve de jantar com alto teor de ferro.',
    10, 1, 210, '4.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '3 ovos,
1 punhado de espinafre fresco, 
2 colheres de ricota esfarelada.'),

    (5, 1, 1, 'Creme de Abóbora com Gengibre', 'Sopa termogênica e reconfortante para dias frios.',
    30, 3, 160, '5.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '500g de abóbora cabotiá,
1 pedaço de gengibre, 
1 cebola, 
1 alho, 
sal marinho.'),

    (6, 1, 5, 'Smoothie Antioxidante de Frutas Vermelhas', 'Bebida refrescante rica em flavonoides.',
    5, 1, 190, '6.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 xícara de morangos e mirtilos,
150ml de leite de amêndoas, 
mel opcional.'),

    (7, 1, 1, 'Frango com Crosta de Ervas', 'Peito de frango suculento com temperos naturais.',
    25, 1, 280, '7.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '150g de peito de frango,
alecrim, 
tomilho, 
salsinha, 
fio de azeite.'),

    (8, 1, 2, 'Pudim de Chia e Coco', 'Lanche da tarde que promove saciedade e saúde intestinal.',
    10, 2, 220, '8.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '3 colheres de sopa de chia,
200ml de leite de coco caseiro, 
adoçante estévia.'),

    (9, 1, 1, 'Guacamole Proteico', 'Gorduras boas do abacate com um toque de coentro.',
    10, 4, 150, '9.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 abacate maduro,
tomate picado, 
cebola roxa, 
limão, 
coentro, 
sal.'),

    (10, 1, 2, 'Muffins de Banana e Cacau', 'Lanche doce fit para crianças e adultos.',
    30, 6, 180, '10.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '3 bananas,
2 ovos, 
1 xícara de aveia, 
2 colheres de cacau 100%.'),

    (11, 1, 3, 'Salmão Grelhado com Manga', 'Prato sofisticado unindo proteínas e o doce da fruta.',
    20, 1, 350, '11.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 filé de salmão,
cubos de manga fresca, 
pimenta dedo-de-moça, 
limão.'),

    (12, 1, 1, 'Espaguete de Abobrinha ao Pesto', 'Substituto leve para massas tradicionais, baixo em carboidratos.',
    15, 1, 195, '12.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '2 abobrinhas cortadas em tiras finas,
manjericão, 
castanhas, 
azeite, 
alho.'),

    (13, 1, 1, 'Crepioca de Queijo Branco', 'Clássico do lanche fit, prático e versátil.',
    8, 1, 230, '13.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 ovo,
2 colheres de goma de tapioca, 
1 fatia de queijo minas frescal.'),

    (14, 1, 4, 'Molho de Iogurte com Hortelã', 'Acompanhamento refrescante para saladas e grelhados.',
    5, 5, 45, '14.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 pote de iogurte desnatado,
hortelã fresca, 
sal, 
limão.'),

    (15, 1, 1, 'Salada de Grão-de-Bico Mediterrânea', 'Fonte de fibras e proteínas vegetais.',
    15, 2, 290, '15.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 xícara de grão-de-bico cozido,
pepino, 
tomate cereja, 
azeitonas, 
azeite.'),

    (16, 1, 1, 'Arroz de Couve-Flor', 'Excelente opção low-carb para substituir o arroz branco.',
    15, 2, 70, '16.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 couve-flor triturada,
cebola picada, 
salsinha, 
sal.'),

    (17, 1, 3, 'Espetinho de Frango com Abacaxi', 'Combinação agridoce grelhada deliciosa.',
    20, 2, 260, '17.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '200g de frango em cubos,
fatias de abacaxi, 
pimentão verde.'),

    (18, 1, 1, 'Chips de Batata Doce Assada', 'Snack saudável para substituir batata frita industrializada.',
    40, 2, 140, '18.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '1 batata doce grande fatiada fina,
páprica defumada, 
azeite.'),

    (19, 1, 4, 'Molho de Tomate Caseiro Rústico', 'Livre de conservantes e açúcares dos molhos prontos.',
    45, 4, 60, '19.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '6 tomates maduros,
1 manjericão, 
1 cebola, 
1 alho, 
azeite de oliva.'),

    (20, 1, 2, 'Sorvete de Banana "Nicecream"', 'Apenas um ingrediente para uma sobremesa gelada e saudável.',
    5, 2, 110, '20.png', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
    '3 bananas congeladas picadas.');

-----------------------------------------------------------------------------------------------------------
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

    (9, 6, 1, 'Liquidificador', 'Coloque as frutas vermelhas e o leite de amêndoas no liquidificador.', CURRENT_TIMESTAMP),
    (10, 6, 2, 'Mistura', 'Bata até obter uma consistência homogênea e sirva gelado.', CURRENT_TIMESTAMP),

    (11, 7, 1, 'Temperagem', 'Pique as ervas bem finas e misture com um pouco de azeite.', CURRENT_TIMESTAMP),
    (12, 7, 2, 'Crosta', 'Pressione a mistura de ervas sobre o frango e grelhe até dourar.', CURRENT_TIMESTAMP),

    (13, 8, 1, 'Hidratação', 'Misture a chia com o leite de coco e o adoçante em um pote.', CURRENT_TIMESTAMP),
    (14, 8, 2, 'Espera', 'Deixe descansar na geladeira por pelo menos 4 horas antes de consumir.', CURRENT_TIMESTAMP),

    (15, 9, 1, 'Preparo', 'Amasse o abacate grosseiramente e misture os demais ingredientes picados.', CURRENT_TIMESTAMP),

    (16, 10, 1, 'Massa', 'Bata os ovos, amasse as bananas e misture com a aveia e o cacau.', CURRENT_TIMESTAMP),
    (17, 10, 2, 'Assar', 'Coloque em forminhas e leve ao forno médio por cerca de 20 minutos.', CURRENT_TIMESTAMP),

    (18, 11, 1, 'Grelha', 'Tempere o salmão com sal e limão e grelhe o peixe.', CURRENT_TIMESTAMP),
    (19, 11, 2, 'Molho', 'Pique a manga em cubos e sirva por cima do salmão com a pimenta.', CURRENT_TIMESTAMP),

    (20, 12, 1, 'Corte', 'Corte a abobrinha em fios longos como se fosse espaguete.', CURRENT_TIMESTAMP),
    (21, 12, 2, 'Saltear', 'Refogue rapidamente a abobrinha e misture o molho pesto.', CURRENT_TIMESTAMP),

    (22, 13, 1, 'Massa', 'Bata o ovo com a tapioca e leve à frigideira antiaderente.', CURRENT_TIMESTAMP),
    (23, 13, 2, 'Recheio', 'Coloque o queijo no meio, dobre a massa e deixe derreter.', CURRENT_TIMESTAMP),

    (24, 14, 1, 'Mistura', 'Pique a hortelã bem fina e misture vigorosamente ao iogurte e limão.', CURRENT_TIMESTAMP),

    (25, 15, 1, 'Montagem', 'Em uma tigela, misture o grão-de-bico com os vegetais picados e tempere.', CURRENT_TIMESTAMP),

    (26, 16, 1, 'Refogado', 'Refogue a cebola e o alho, adicione a couve-flor triturada e mexa por 5 min.', CURRENT_TIMESTAMP),

    (27, 17, 1, 'Montagem', 'Intercale cubos de frango e abacaxi nos espetos.', CURRENT_TIMESTAMP),
    (28, 17, 2, 'Cozimento', 'Grelhe ouasse até o frango estar cozido e o abacaxi levemente dourado.', CURRENT_TIMESTAMP),

    (29, 18, 1, 'Corte', 'Corte as batatas em fatias quase transparentes.', CURRENT_TIMESTAMP),
    (30, 18, 2, 'Assar', 'Arrume em uma assadeira sem sobrepor e asse até ficarem crocantes.', CURRENT_TIMESTAMP),

    (31, 19, 1, 'Cozimento', 'Cozinhe os tomates com os temperos em fogo baixo até desmanchar.', CURRENT_TIMESTAMP),

    (32, 20, 1, 'Bater', 'Bata as bananas congeladas no processador até virar creme.', CURRENT_TIMESTAMP);

-----------------------------------------------------------------------------------------------------------
INSERT INTO public.receita_restricao (id, receita_id, restricao_id, created) 
VALUES
    (1, 1, 1, CURRENT_TIMESTAMP),
    (2, 1, 8, CURRENT_TIMESTAMP),

    (3, 2, 7, CURRENT_TIMESTAMP),
    (4, 2, 2, CURRENT_TIMESTAMP),

    (5, 3, 7, CURRENT_TIMESTAMP),
    (6, 3, 9, CURRENT_TIMESTAMP),

    (7, 4, 8, CURRENT_TIMESTAMP),
    (8, 4, 2, CURRENT_TIMESTAMP),

    (9, 5, 7, CURRENT_TIMESTAMP),

    (10, 6, 7, CURRENT_TIMESTAMP),
    (11, 6, 1, CURRENT_TIMESTAMP),
    (12, 6, 2, CURRENT_TIMESTAMP),

    (13, 7, 1, CURRENT_TIMESTAMP),
    (14, 7, 2, CURRENT_TIMESTAMP),

    (15, 8, 7, CURRENT_TIMESTAMP),
    (16, 8, 9, CURRENT_TIMESTAMP),

    (17, 9, 7, CURRENT_TIMESTAMP),

    (18, 10, 8, CURRENT_TIMESTAMP),
    (19, 10, 1, CURRENT_TIMESTAMP),

    (20, 11, 1, CURRENT_TIMESTAMP),
    (21, 11, 2, CURRENT_TIMESTAMP),

    (22, 12, 7, CURRENT_TIMESTAMP),
    (23, 12, 2, CURRENT_TIMESTAMP),

    (24, 13, 8, CURRENT_TIMESTAMP),
    (25, 13, 2, CURRENT_TIMESTAMP),

    (26, 14, 8, CURRENT_TIMESTAMP),
    (27, 14, 2, CURRENT_TIMESTAMP),

    (28, 15, 7, CURRENT_TIMESTAMP),

    (29, 16, 7, CURRENT_TIMESTAMP),
    (30, 16, 9, CURRENT_TIMESTAMP),

    (31, 17, 1, CURRENT_TIMESTAMP),
    (32, 17, 2, CURRENT_TIMESTAMP),

    (33, 18, 7, CURRENT_TIMESTAMP),

    (34, 19, 7, CURRENT_TIMESTAMP),

    (35, 20, 7, CURRENT_TIMESTAMP),
    (36, 20, 1, CURRENT_TIMESTAMP);

-- Inserção dos documentos legais TEMPORARIO (SERÃO RETIRADOS DEPOIS, POIS ADMINISTRADOR CADASTRARÁ PELO SISTEMA)
-- 1. Política de Privacidade (Comum para ambos)
INSERT INTO public.documentos_legais (id, tipo, versao, texto, activated, created)
VALUES
    (1,
'POLITICA_PRIVACIDADE',
'1.0',
'**1. Introdução**
O SafeNutri valoriza a sua privacidade e está comprometido em proteger os seus dados pessoais. Esta Política de Privacidade explica como coletamos, usamos e protegemos as informações que você nos fornece ao se cadastrar e utilizar nossa plataforma, em conformidade com a Lei Geral de Proteção de Dados (LGPD - Lei nº 13.709/2018).

**2. Quais dados coletamos**
Ao criar uma conta no SafeNutri, coletamos as seguintes informações:
* **Usuários comuns:** Nome, endereço de e-mail, senha criptografada e informações sobre restrições alimentares fornecidas voluntariamente.
* **Nutricionistas:** Nome, endereço de e-mail, senha criptografada e o número do Conselho Regional de Nutricionistas (CRN).

**3. Como usamos os seus dados**
Utilizamos seus dados exclusivamente para o funcionamento da plataforma:
* Para permitir o login e a identificação segura no sistema.
* Para personalizar a sua experiência e gerenciar suas restrições alimentares.
* No caso dos nutricionistas, o CRN é utilizado para verificação de registro profissional e para dar os devidos créditos de autoria nas receitas públicas.
* Para enviar comunicações importantes sobre a sua conta ou mudanças nos nossos Termos.

**4. Armazenamento e Segurança**
O SafeNutri adota as melhores práticas de segurança da informação. A sua senha é armazenada de forma criptografada em nosso banco de dados, o que significa que nem mesmo os administradores do sistema têm acesso a ela. Seus dados são guardados em servidores seguros, com acesso restrito.

**5. Compartilhamento de Dados**
Nós não vendemos, alugamos ou comercializamos os seus dados pessoais com terceiros. As informações dos usuários são mantidas em sigilo. As receitas cadastradas por nutricionistas se tornam públicas e exibirão o nome e CRN do autor por questões de transparência e responsabilidade técnica. Seus dados só serão compartilhados com autoridades competentes caso haja uma ordem judicial ou exigência legal.

**6. Seus Direitos (LGPD)**
Você tem total controle sobre os seus dados. A qualquer momento, você pode:
* Acessar as informações que temos sobre você.
* Corrigir dados incompletos, inexatos ou desatualizados.
* Solicitar a exclusão definitiva da sua conta e de todos os seus dados pessoais do nosso banco de dados (ressalvada a guarda necessária para cumprimento de obrigação legal).

**7. Contato**
Caso tenha qualquer dúvida sobre esta Política de Privacidade ou sobre o tratamento dos seus dados, entre em contato conosco através do e-mail: **safenutri.iftm@gmail.com**', 
    TRUE, 
    CURRENT_TIMESTAMP
),

    (2,
'TERMO_USO_COMUM',
'1.0',
'**1. Aceitação dos Termos**
Ao marcar a caixa de seleção e concluir o cadastro, o usuário concorda integralmente com as regras descritas neste documento. Caso não concorde, o cadastro não poderá ser concluído.

**2. Natureza do Serviço e Isenção de Responsabilidade Médica**
O SafeNutri é uma plataforma de recomendação e compartilhamento de receitas adaptadas para restrições alimentares. As informações e receitas contidas aqui **não substituem, em hipótese alguma, o diagnóstico, aconselhamento ou acompanhamento de um médico ou nutricionista**. O usuário é o único responsável por verificar os ingredientes antes de consumi-los.

**3. Receitas Privadas**
As receitas cadastradas por você (usuário comum) são estritamente privadas. Elas ficarão visíveis apenas no seu perfil e não serão compartilhadas publicamente na plataforma.

**4. Solicitação de Novas Restrições**
O usuário poderá solicitar a adição de novas restrições alimentares ao banco de dados do sistema. Todas as solicitações passarão por uma análise do administrador, que tem total liberdade para aprovar ou rejeitar o pedido, sem necessidade de aviso prévio.

**5. Uso da Plataforma e Banimento**
É proibido utilizar a plataforma para fins ilegais. O descumprimento de qualquer regra pode resultar na suspensão ou exclusão permanente da sua conta.', 
    TRUE, 
    CURRENT_TIMESTAMP
),

    (3,
'TERMO_COMPROMISSO',
'1.0',
'**1. Aceitação e Identificação**
Ao marcar a caixa de seleção, o profissional concorda com este termo e declara, sob as penas da lei, que todas as informações fornecidas no cadastro são verdadeiras.

**2. Veracidade do CRN e Falsidade Ideológica**
O usuário declara ser o titular legítimo do Conselho Regional de Nutricionistas (CRN) informado no momento do cadastro. O uso de dados de terceiros ou a falsificação de identidade constitui crime de **Falsidade Ideológica (Art. 299 do Código Penal Brasileiro)**. A plataforma reserva-se o direito de banir imediatamente a conta e relatar a fraude às autoridades competentes e ao Conselho Federal de Nutricionistas (CFN) caso seja detectada qualquer irregularidade.

**3. Responsabilidade Técnica pelas Receitas**
Ao contrário dos usuários comuns, as receitas cadastradas pelo perfil Nutricionista **se tornam públicas para toda a plataforma**. O profissional assume total responsabilidade técnica pelas receitas que publicar, garantindo que os ingredientes informados são rigorosamente seguros e adequados para as restrições alimentares indicadas.

**4. Licença de Uso e Direitos Autorais**
Ao publicar uma receita, o nutricionista concede ao SafeNutri o direito gratuito de exibir, formatar e distribuir esse conteúdo dentro do sistema, garantindo sempre que os devidos créditos de autoria (nome e CRN) fiquem visíveis para os usuários.

**5. Moderação e Exclusão de Conteúdo**
A administração do sistema se reserva o direito de ocultar ou excluir qualquer receita pública que receba denúncias de usuários ou que seja considerada insegura para a comunidade, visando a proteção da saúde dos utilizadores.', 
    TRUE, 
    CURRENT_TIMESTAMP
);
-----------------------------------------------------------------------------------------------------------
INSERT INTO public.aceito_termos (id, user_id, documento_id, created)
VALUES
    (1,1,1,CURRENT_TIMESTAMP),
    (2,1,3,CURRENT_TIMESTAMP),

    (3,2,1,CURRENT_TIMESTAMP),
    (4,2,3,CURRENT_TIMESTAMP),

    (5,3,1,CURRENT_TIMESTAMP),
    (6,3,2,CURRENT_TIMESTAMP),

    (7,4,1,CURRENT_TIMESTAMP),
    (8,4,2,CURRENT_TIMESTAMP);
-----------------------------------------------------------------------------------------------------------
INSERT INTO public.status_denuncia (id, status)
VALUES
    (1, 'AGUARDANDO'),
    (2, 'CONCLUÍDA');