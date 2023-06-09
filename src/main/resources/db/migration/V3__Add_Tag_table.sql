CREATE TABLE tag
(
    book  BIGINT       NOT NULL references book (id),
    name  VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL
);
