CREATE TABLE book
(
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    isbn               VARCHAR(255) UNIQUE   NOT NULL,
    author             VARCHAR(255)          NOT NULL,
    title              VARCHAR(255)          NOT NULL,
    price              float8                NOT NULL,
    version            INTEGER               NOT NULL,
    created_date       TIMESTAMP             NOT NULL,
    last_modified_date TIMESTAMP             NOT NULL
);
