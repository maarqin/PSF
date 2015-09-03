set sql_safe_updates = 0;
/* Deleta todos os registros */
DELETE FROM documento;
DELETE FROM solicitacao;
DELETE FROM colegiado_has_usuario;
DELETE FROM colegiado;
DELETE FROM usuario;

/* Zerar auto_increment ids */
ALTER TABLE colegiado AUTO_INCREMENT = 1;
ALTER TABLE colegiado_has_usuario AUTO_INCREMENT = 1;
ALTER TABLE documento AUTO_INCREMENT = 1;
ALTER TABLE solicitacao AUTO_INCREMENT = 1;
ALTER TABLE usuario AUTO_INCREMENT = 1;

INSERT INTO `bdpsf`.`colegiado` (`nome`, `quantidadecursos`, `estado`) VALUES ('Agrárias', '2', 'Ativo');
INSERT INTO `bdpsf`.`colegiado` (`nome`, `quantidadecursos`, `estado`) VALUES ('Saúde', '6', 'Ativo');
INSERT INTO `bdpsf`.`colegiado` (`nome`, `quantidadecursos`, `estado`) VALUES ('Humanas', '2', 'Ativo');
INSERT INTO `bdpsf`.`colegiado` (`nome`, `quantidadecursos`, `estado`) VALUES ('Exatas', '2', 'Ativo');
INSERT INTO `bdpsf`.`colegiado` (`nome`, `quantidadecursos`, `estado`) VALUES ('Sociais', '4', 'Ativo');
INSERT INTO `bdpsf`.`colegiado` (`nome`, `quantidadecursos`, `estado`) VALUES ('Engenharia', '1', 'Ativo');
INSERT INTO `bdpsf`.`colegiado` (`nome`, `quantidadecursos`, `estado`) VALUES ('Técnico', '3', 'Ativo');

/* Selects alls */
SELECT * FROM colegiado;
SELECT * FROM usuario;
SELECT * FROM colegiado_has_usuario;
SELECT * FROM documento;
SELECT * FROM solicitacao;

INSERT INTO `bdpsf`.`usuario` (`tipo`, `nome`, `email`, `senha`, `matricula`, `estado`) VALUES ('Administrador', 'NomeAdmin', 'a@a.com', 'aaa', '111111111', 'Ativo');
INSERT INTO `bdpsf`.`usuario` (`tipo`, `nome`, `email`, `senha`, `matricula`, `estado`) VALUES ('Funcionario', 'NomeFunc', 'f@f.com', 'fff', '222222222', 'Ativo');
INSERT INTO `bdpsf`.`usuario` (`tipo`, `nome`, `email`, `senha`, `matricula`, `estado`) VALUES ('Professor', 'NomeProf', 'p@p.com', 'ppp', '333333333', 'Ativo');





















select * from solicitacao where solicitacao.usuario_idusuario = 22;


insert into colegiado_has_usuario (colegiado_idcolegiado, usuario_idusuario)values(2,2);
select * from colegiado_has_usuario;

SELECT u.nome, u.email, sum(d.quantidadepaginas) from usuario u 
join solicitacao s on (s.usuario_idusuario = u.idusuario)
join documento d on (d.solicitacao_idsolicitacao = s.idsolicitacao)
join colegiado c on (c.idcolegiado = s.colegiado_idcolegiado) 
where c.idcolegiado = 1
and 
u.idusuario = 4
;

SELECT * FROM solicitacao;
SELECT * FROM documento;

select * from usuario u 
where u.idusuario = solicitacao.usuario_idusuario and
solicitacao.colegiado_idcolegiado = colegiado.idcolegiado and
colegiado.idcolegiado = 1;

SELECT (d.quantidadepaginas)  as total from usuario u 
join solicitacao s on (s.usuario_idusuario = u.idusuario)
join documento d on (d.solicitacao_idsolicitacao = s.idsolicitacao)
join colegiado c on (c.idcolegiado = s.colegiado_idcolegiado) 
where c.idcolegiado = 1;