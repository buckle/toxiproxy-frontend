CREATE TABLE toxiproxy_lob_entity (
  id                     BIGINT        NOT NULL AUTO_INCREMENT,
  data                   CLOB          NOT NULL,
  create_timestamp       DATETIME      NOT NULL,
  update_timestamp       DATETIME      NOT NULL,
  PRIMARY KEY (id)
);