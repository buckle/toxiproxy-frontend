CREATE TABLE proxy_entity (
  id                     BIGINT        NOT NULL AUTO_INCREMENT,
  name                   VARCHAR       NOT NULL,
  listen                 VARCHAR       NOT NULL,
  upstream               VARCHAR       NOT NULL,
  enabled                BIT,
  create_timestamp       DATETIME      NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_proxy_name UNIQUE (`name`)
);