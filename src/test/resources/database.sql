
-- test_db1
CREATE DATABASE `test_db1` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' COLLATE 'utf8mb4_general_ci';
CREATE TABLE `test_db1`.`t_test_table` (
    `test_code` varchar(40) COLLATE utf8mb4_general_ci NOT NULL,
    `test_name` varchar(80) COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`test_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `test_db1`.`t_test_table` (`test_code`, `test_name`) VALUES ('tab1', 'the name of tab1');


-- test_db2
CREATE DATABASE `test_db2` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' COLLATE 'utf8mb4_general_ci';
CREATE TABLE `test_db2`.`t_test_table` (
    `test_code` varchar(40) COLLATE utf8mb4_general_ci NOT NULL,
    `test_name` varchar(80) COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`test_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `test_db2`.`t_test_table` (`test_code`, `test_name`) VALUES ('tab2', 'the name of tab2');


-- test_db3
CREATE DATABASE `test_db3` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' COLLATE 'utf8mb4_general_ci';
CREATE TABLE `test_db3`.`t_test_table` (
    `test_code` varchar(40) COLLATE utf8mb4_general_ci NOT NULL,
    `test_name` varchar(80) COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`test_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `test_db3`.`t_test_table` (`test_code`, `test_name`) VALUES ('tab3', 'the name of tab3');
