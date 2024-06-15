CREATE TABLE authors (
                         id bigserial,
                         full_name varchar(255),
                         PRIMARY KEY (id)
);

CREATE TABLE genres (
                        id bigserial,
                        name varchar(255),
                        PRIMARY KEY (id)
);

CREATE TABLE books (
                       id bigserial,
                       title varchar(255),
                       author_id bigint REFERENCES authors (id) ON DELETE CASCADE,
                       PRIMARY KEY (id)
);

CREATE TABLE books_genres (
                              book_id bigint REFERENCES books(id) ON DELETE CASCADE,
                              genre_id bigint REFERENCES genres(id) ON DELETE CASCADE,
                              PRIMARY KEY (book_id, genre_id)
);

CREATE TABLE comments (
                       id bigserial,
                       text varchar(255),
                       book_id bigint REFERENCES books (id) ON DELETE CASCADE,
                       PRIMARY KEY (id)
)