SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS usertable;
CREATE TABLE usertable (
    ycsb_key int PRIMARY KEY,
    vid      bigint,
    field1   varchar(100),
    field2   varchar(100),
    field3   varchar(100),
    field4   varchar(100),
    field5   varchar(100),
    field6   varchar(100),
    field7   varchar(100),
    field8   varchar(100),
    field9   varchar(100),
    field10  varchar(100)
);

DROP TABLE IF EXISTS ycsb_conflict;
CREATE TABLE ycsb_conflict (
    ycsb_key int PRIMARY KEY,
    vid      bigint
);

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;