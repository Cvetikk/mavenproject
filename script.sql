create table COUNTER
(
  count int2,
  key   VARCHAR
);

create unique index COUNTER_IDX on COUNTER (KEY);