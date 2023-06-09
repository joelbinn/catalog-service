CREATE TABLE book_reference (
    book BIGINT NOT NULL references book(id),
    referenced_book BIGINT NOT NULL references book(id)
);

