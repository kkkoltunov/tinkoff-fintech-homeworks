CREATE TABLE students_events
(
    ID         UUID PRIMARY KEY NOT NULL,
    STUDENT_ID BIGINT           NOT NULL,
    STATUS     VARCHAR          NOT NULL
        check (STATUS in ('CREATED', 'UPDATED', 'DELETED'))
);

