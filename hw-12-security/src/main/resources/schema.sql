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
);

CREATE TABLE users (
    id bigserial,
    username varchar(255),
    password varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE roles (
    id bigserial,
    name varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE user_roles (
    user_id bigint REFERENCES users(id) ON DELETE CASCADE,
    role_id bigint REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY(user_id, role_id)
)