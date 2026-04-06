INSERT INTO genero (genero) VALUES ('MASCULINO');
INSERT INTO genero (genero) VALUES ('FEMININO');

INSERT INTO role (role) VALUES ('ADMIN');
INSERT INTO role (role) VALUES ('NUTRICIONISTA');
INSERT INTO role (role) VALUES ('USER');

INSERT INTO restricao_alimentar (restricao, descricao, approved, activated) 
    VALUES ('LACTOSE', 'Restrição para alérgicos a lactose', TRUE, TRUE);

INSERT INTO restricao_alimentar (restricao, descricao, approved, activated) 
    VALUES ('GLÚTEN', 'Restrição para alérgicos a glúten', TRUE, TRUE);

INSERT INTO tipo_receita (tipo_receita) VALUES ('SALGADA');
INSERT INTO tipo_receita (tipo_receita) VALUES ('DOCE');
INSERT INTO tipo_receita (tipo_receita) VALUES ('AGRIDOCE');
INSERT INTO tipo_receita (tipo_receita) VALUES ('MOLHO');
INSERT INTO tipo_receita (tipo_receita) VALUES ('BEBIDA');


-- Inserção dos documentos legais TEMPORARIO (SERÃO RETIRADOS DEPOIS, POIS ADMINISTRADOR CADASTRARÁ PELO SISTEMA)

-- 1. Política de Privacidade (Comum para ambos)
INSERT INTO documentos_legais (tipo, versao, texto, activated, created) 
VALUES (
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
);

-- 2. Termos de Uso (Para Usuário Comum)
INSERT INTO documentos_legais (tipo, versao, texto, activated, created) 
VALUES (
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
);

-- 3. Termo de Compromisso (Para Nutricionistas)
INSERT INTO documentos_legais (tipo, versao, texto, activated, created) 
VALUES (
    'TERMO_NUTRI', 
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