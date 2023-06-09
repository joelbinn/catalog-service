ALTER TABLE book
    ADD COLUMN referenced_book BIGINT NULL references book(id);

