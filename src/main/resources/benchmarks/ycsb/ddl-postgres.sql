DROP TABLE IF EXISTS usertable;
CREATE TABLE usertable (
    ycsb_key int PRIMARY KEY,
    vid      bigint NOT NULL,
    field1   text,
    field2   text,
    field3   text,
    field4   text,
    field5   text,
    field6   text,
    field7   text,
    field8   text,
    field9   text,
    field10  text
);

DROP TABLE IF EXISTS ycsb_conflict;
CREATE TABLE ycsb_conflict (
      ycsb_key int PRIMARY KEY,
      vid      bigint NOT NULL
);