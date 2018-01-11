CREATE DATABASE IF NOT EXISTS `ClinicDB`;

CREATE TABLE IF NOT EXISTS `clinicdb`.`Doctors` (
  `PWZ`     CHAR(7)      NOT NULL,
  `name`    VARCHAR(128) NOT NULL,
  `surname` VARCHAR(128) NOT NULL,
  `phone`   VARCHAR(128) NOT NULL,
  PRIMARY KEY (`PWZ`)
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `clinicdb`.`Office Hours` (
  `doctor`    CHAR(7)                                                                             NOT NULL,
  `day`       ENUM ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') NOT NULL,
  `beginning` TIME                                                                                NOT NULL,
  `end`       TIME                                                                                NOT NULL,
  CONSTRAINT BeginningEnd CHECK (beginning < end),
  FOREIGN KEY (`doctor`) REFERENCES `doctors` (`PWZ`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB;

DELIMITER //
CREATE TRIGGER checkTime
  BEFORE INSERT
  ON `Office Hours`
  FOR EACH ROW
  BEGIN
    IF NEW.beginning > NEW.end
    THEN
      SIGNAL SQLSTATE '12345'
      SET MESSAGE_TEXT = 'check constraint on Office Hours failed';
    END IF;
  END
//
DELIMITER ;

CREATE TABLE IF NOT EXISTS `clinicdb`.`Patients` (
  `PESEL`       CHAR(11)     NOT NULL,
  `name`        VARCHAR(128) NOT NULL,
  `surname`     VARCHAR(128) NOT NULL,
  `birthday`    DATE         NOT NULL,
  `city`        VARCHAR(128) NOT NULL,
  `street`      VARCHAR(128) NOT NULL,
  `house numer` INT UNSIGNED NOT NULL,
  `flat number` INT UNSIGNED NOT NULL,
  `postal code` CHAR(6)      NOT NULL,
  `post office` VARCHAR(128) NOT NULL,
  `phone`       VARCHAR(128) NOT NULL,
  PRIMARY KEY (`PESEL`)
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `clinicdb`.`Visits` (
  `ID`           INT      NOT NULL AUTO_INCREMENT,
  `Doctor`       CHAR(7)  NOT NULL,
  `Patient`      CHAR(11) NOT NULL,
  `confirmation` BOOLEAN  NOT NULL DEFAULT FALSE,
  `time`         TIME     NOT NULL,
  `date`         DATE     NOT NULL,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`Doctor`) REFERENCES `doctors` (`PWZ`),
  FOREIGN KEY (`Patient`) REFERENCES `Patients` (`PESEL`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB;
/*
DELIMITER //
CREATE TRIGGER checkDate
  BEFORE INSERT
  ON `Visits`
  FOR EACH ROW
  BEGIN
    IF NEW.date < CURRENT_DATE AND NEW.time < CURRENT_TIME
    THEN
      SIGNAL SQLSTATE '12345'
      SET MESSAGE_TEXT = 'check constraint on Visits-date failed';
    END IF;
  END
//
DELIMITER ;
*/

-- Byc moze trzeba tam dodac 1 przy przeliczaniu day of week na enuma
DELIMITER //
CREATE TRIGGER checkDoctorOfficeHoure
  BEFORE INSERT
  ON `Visits`
  FOR EACH ROW
  BEGIN
    IF NEW.time NOT BETWEEN (
      SELECT H.beginning
      FROM Doctors D
        JOIN `Office Hours` H ON D.PWZ = H.doctor
      WHERE D.PWZ = NEW.Doctor AND H.day = DAYOFWEEK(NEW.date))
    AND
    (SELECT H.end
     FROM Doctors D
       JOIN `Office Hours` H ON D.PWZ = H.doctor
     WHERE D.PWZ = NEW.Doctor AND H.day = DAYOFWEEK(NEW.date))
    THEN
      SIGNAL SQLSTATE '12345'
      SET MESSAGE_TEXT = 'check constraint on Office Hours failed';
    END IF;
    IF NEW.date < CURRENT_DATE AND NEW.time < CURRENT_TIME
    THEN
      SIGNAL SQLSTATE '12345'
      SET MESSAGE_TEXT = 'check constraint on Visits-date failed';
    END IF;
  END
//
DELIMITER ;

CREATE TABLE IF NOT EXISTS `clinicdb`.`diseases` (
  `ID`   INT          NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `clinicdb`.`medicines` (
  `ID`   INT          NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `clinicdb`.`visit history` (
  `ID`       INT      NOT NULL AUTO_INCREMENT,
  `visit_ID` INT      NOT NULL,
  `advices`  LONGTEXT NULL     DEFAULT NULL,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`visit_ID`) REFERENCES `Visits` (`ID`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `clinicdb`.`recognition` (
  `visit_ID` INT NOT NULL,
  `disease`  INT NOT NULL,
  FOREIGN KEY (`visit_ID`) REFERENCES `visit history` (`ID`)
    ON DELETE CASCADE,
  FOREIGN KEY (`disease`) REFERENCES `diseases` (`ID`)
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `clinicdb`.`prescription` (
  `visit_ID` INT NOT NULL,
  `medicine` INT NOT NULL,
  FOREIGN KEY (`visit_ID`) REFERENCES `visit history` (`ID`)
    ON DELETE CASCADE,
  FOREIGN KEY (`medicine`) REFERENCES `medicines` (`ID`)
)
  ENGINE = InnoDB;