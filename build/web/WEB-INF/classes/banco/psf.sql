-- MySQL Script generated by MySQL Workbench
-- 08/13/15 14:36:30
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bdpsf
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `bdpsf` ;

-- -----------------------------------------------------
-- Schema bdpsf
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bdpsf` DEFAULT CHARACTER SET utf8 ;
USE `bdpsf` ;

-- -----------------------------------------------------
-- Table `bdpsf`.`colegiado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdpsf`.`colegiado` (
  `idcolegiado` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `nome` VARCHAR(45) NOT NULL COMMENT '',
  `quantidadecursos` INT(11) NOT NULL COMMENT '',
  `estado` VARCHAR(45) NOT NULL COMMENT '',
  PRIMARY KEY (`idcolegiado`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdpsf`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdpsf`.`usuario` (
  `idusuario` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `tipo` VARCHAR(45) NOT NULL COMMENT '',
  `nome` VARCHAR(45) NOT NULL COMMENT '',
  `email` VARCHAR(45) NOT NULL COMMENT '',
  `senha` VARCHAR(128) NOT NULL COMMENT '',
  `matricula` INT(11) NOT NULL COMMENT '',
  `estado` VARCHAR(45) NOT NULL COMMENT '',
  PRIMARY KEY (`idusuario`)  COMMENT '',
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)  COMMENT '',
  UNIQUE INDEX `matricula_UNIQUE` (`matricula` ASC)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdpsf`.`colegiado_has_usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdpsf`.`colegiado_has_usuario` (
  `id_colegiado_has_usuario` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `colegiado_idcolegiado` INT(11) NOT NULL COMMENT '',
  `usuario_idusuario` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`id_colegiado_has_usuario`)  COMMENT '',
  INDEX `fk_usuario` (`usuario_idusuario` ASC)  COMMENT '',
  INDEX `fk_colegiado` (`colegiado_idcolegiado` ASC)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdpsf`.`solicitacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdpsf`.`solicitacao` (
  `idsolicitacao` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `numprotocolo` INT(11) NOT NULL COMMENT '',
  `dataaplicacao` DATETIME NOT NULL COMMENT '',
  `periodoaplicacao` VARCHAR(45) NOT NULL COMMENT '',
  `datacriacao` DATETIME NOT NULL COMMENT '',
  `estado` VARCHAR(45) NOT NULL COMMENT '',
  `motivorecusa` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `atendente` INT(11) NULL DEFAULT NULL COMMENT '',
  `usuario_idusuario` INT(11) NOT NULL COMMENT '',
  `colegiado_idcolegiado` INT(11) NOT NULL COMMENT '',
  `frenteVerso` TINYINT(4) NOT NULL COMMENT '',
  PRIMARY KEY (`idsolicitacao`)  COMMENT '',
  INDEX `fk_usuario` (`usuario_idusuario` ASC)  COMMENT '',
  INDEX `fk_colegiado2` (`colegiado_idcolegiado` ASC)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `bdpsf`.`documento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bdpsf`.`documento` (
  `iddocumento` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `nomedocumento` VARCHAR(45) NOT NULL COMMENT '',
  `quantidadecopias` INT(11) NOT NULL COMMENT '',
  `enderecodocumento` VARCHAR(100) NOT NULL COMMENT '',
  `quantidadepaginas` INT(11) NOT NULL COMMENT '',
  `solicitacao_idsolicitacao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`iddocumento`)  COMMENT '',
  INDEX `fk_solicitacao` (`solicitacao_idsolicitacao` ASC)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
