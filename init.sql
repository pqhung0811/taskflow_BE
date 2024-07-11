-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema task_flow
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema task_flow
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `task_flow` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `task_flow` ;

-- -----------------------------------------------------
-- Table `task_flow`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(1024) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`project` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `start_date` DATETIME(6) NULL DEFAULT NULL,
  `manager_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK2g58lgoxy5typ93ob5k7unehp` (`manager_id` ASC) VISIBLE,
  CONSTRAINT `FK2g58lgoxy5typ93ob5k7unehp`
    FOREIGN KEY (`manager_id`)
    REFERENCES `task_flow`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 34
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`task`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NULL DEFAULT NULL,
  `advance` INT NULL DEFAULT NULL,
  `deadline` DATETIME(6) NULL DEFAULT NULL,
  `state` VARCHAR(45) NULL DEFAULT NULL,
  `start_time` DATETIME(6) NULL DEFAULT NULL,
  `project_id` INT NULL DEFAULT NULL,
  `user_id` INT NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `priority` INT NULL DEFAULT NULL,
  `category` INT NULL DEFAULT NULL,
  `end_time` DATETIME(6) NULL DEFAULT NULL,
  `estimate_time` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKk8qrwowg31kx7hp93sru1pdqa` (`project_id` ASC) VISIBLE,
  INDEX `FK2hsytmxysatfvt0p1992cw449` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK2hsytmxysatfvt0p1992cw449`
    FOREIGN KEY (`user_id`)
    REFERENCES `task_flow`.`user` (`id`),
  CONSTRAINT `FKk8qrwowg31kx7hp93sru1pdqa`
    FOREIGN KEY (`project_id`)
    REFERENCES `task_flow`.`project` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 63
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(1024) NULL DEFAULT NULL,
  `date` DATETIME NULL DEFAULT NULL,
  `task_id` INT NULL DEFAULT NULL,
  `author_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKfknte4fhjhet3l1802m1yqa50` (`task_id` ASC) VISIBLE,
  INDEX `FKh1gtv412u19wcbx22177xbkjp` (`author_id` ASC) VISIBLE,
  CONSTRAINT `FKfknte4fhjhet3l1802m1yqa50`
    FOREIGN KEY (`task_id`)
    REFERENCES `task_flow`.`task` (`id`),
  CONSTRAINT `FKh1gtv412u19wcbx22177xbkjp`
    FOREIGN KEY (`author_id`)
    REFERENCES `task_flow`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 44
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`file_attachment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`file_attachment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `file_name` VARCHAR(255) NULL DEFAULT NULL,
  `file_path` VARCHAR(255) NULL DEFAULT NULL,
  `task_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKv6qdbkvtc1bd7svknoflybii` (`task_id` ASC) VISIBLE,
  CONSTRAINT `FKv6qdbkvtc1bd7svknoflybii`
    FOREIGN KEY (`task_id`)
    REFERENCES `task_flow`.`task` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 97
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`folder`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`folder` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `parent_folder_id` INT NULL DEFAULT NULL,
  `project_id` INT NULL DEFAULT NULL,
  `update_time` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK57g7veis1gp5wn3g0mp0x57pl` (`parent_folder_id` ASC) VISIBLE,
  INDEX `FKauh6j53qhuhr83xr98j3nna7u` (`project_id` ASC) VISIBLE,
  CONSTRAINT `FK57g7veis1gp5wn3g0mp0x57pl`
    FOREIGN KEY (`parent_folder_id`)
    REFERENCES `task_flow`.`folder` (`id`),
  CONSTRAINT `FKauh6j53qhuhr83xr98j3nna7u`
    FOREIGN KEY (`project_id`)
    REFERENCES `task_flow`.`project` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 19
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`file_share`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`file_share` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `file_name` VARCHAR(255) NULL DEFAULT NULL,
  `file_path` VARCHAR(255) NULL DEFAULT NULL,
  `folder_id` INT NULL DEFAULT NULL,
  `project_id` INT NULL DEFAULT NULL,
  `size` BIGINT NOT NULL,
  `update_time` DATETIME(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK18cvqaw01uak8suvl3gxya0vh` (`folder_id` ASC) VISIBLE,
  INDEX `FKm3w3gckaa6p8d8ar5er9qx2c4` (`project_id` ASC) VISIBLE,
  CONSTRAINT `FK18cvqaw01uak8suvl3gxya0vh`
    FOREIGN KEY (`folder_id`)
    REFERENCES `task_flow`.`folder` (`id`),
  CONSTRAINT `FKm3w3gckaa6p8d8ar5er9qx2c4`
    FOREIGN KEY (`project_id`)
    REFERENCES `task_flow`.`project` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 43
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`hibernate_sequence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`hibernate_sequence` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`image_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`image_data` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `image_data` LONGBLOB NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKmqudlpwgk86qsxm0yxrkycfji` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FKmqudlpwgk86qsxm0yxrkycfji`
    FOREIGN KEY (`user_id`)
    REFERENCES `task_flow`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`notifications`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`notifications` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(255) NULL DEFAULT NULL,
  `user_id` INT NULL DEFAULT NULL,
  `is_read` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK3dt2b80521eynbjg4nehtjnhy` (`user_id` ASC) VISIBLE,
  CONSTRAINT `FK3dt2b80521eynbjg4nehtjnhy`
    FOREIGN KEY (`user_id`)
    REFERENCES `task_flow`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 155
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`project_member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`project_member` (
  `project_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  INDEX `FK6s59w9jalg0dperffu3ri91or` (`user_id` ASC) VISIBLE,
  INDEX `FK103dwxad12nbaxtmnwus4eft2` (`project_id` ASC) VISIBLE,
  CONSTRAINT `FK103dwxad12nbaxtmnwus4eft2`
    FOREIGN KEY (`project_id`)
    REFERENCES `task_flow`.`project` (`id`),
  CONSTRAINT `FK6s59w9jalg0dperffu3ri91or`
    FOREIGN KEY (`user_id`)
    REFERENCES `task_flow`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `task_flow`.`spring_session`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_flow`.`spring_session` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
