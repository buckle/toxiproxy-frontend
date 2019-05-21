CREATE TABLE proxy_entity (
  id                     BIGINT             NOT NULL AUTO_INCREMENT,
  name                   VARCHAR(191)       NOT NULL,
  listen                 VARCHAR(191)       NOT NULL,
  upstream               VARCHAR(191)       NOT NULL,
  enabled                BIT,
  create_timestamp       DATETIME           NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_proxy_name UNIQUE (name)
);