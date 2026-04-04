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