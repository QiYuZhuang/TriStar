SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS checking;
DROP TABLE IF EXISTS savings;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS conflict;

CREATE TABLE accounts (
    custid bigint      NOT NULL,
    name   varchar(64) NOT NULL,
    CONSTRAINT pk_accounts PRIMARY KEY (custid)
);
CREATE INDEX idx_accounts_name ON accounts (name);

CREATE TABLE savings (
    custid bigint NOT NULL,
    bal    float  NOT NULL,
    tid    bigint NOT NULL,
    CONSTRAINT pk_savings PRIMARY KEY (custid),
    FOREIGN KEY (custid) REFERENCES accounts (custid)
);

CREATE TABLE checking (
    custid bigint NOT NULL,
    bal    float  NOT NULL,
    tid    bigint NOT NULL,
    CONSTRAINT pk_checking PRIMARY KEY (custid),
    FOREIGN KEY (custid) REFERENCES accounts (custid)
);

CREATE TABLE conflict (
                          custid bigint      NOT NULL,
                          name   varchar(64) NOT NULL,
                          CONSTRAINT pk_conflict PRIMARY KEY (custid)
);
CREATE INDEX idx_conflict_name ON conflict (name);

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;