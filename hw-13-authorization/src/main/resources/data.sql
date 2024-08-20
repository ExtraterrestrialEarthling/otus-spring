
insert into authors(full_name)
values ('E. L. James'), ('J.K. Rowling'), ('J.R.R. Tolkien');

insert into genres(name)
values ('Dystopian'), ('Fantasy'), ('Adventure'),
       ('Science Fiction'), ('Historical Fiction'), ('Mystery'), ('Erotic');

insert into books(title, author_id, adult_only)
values ('Fifty Shades of Grey', 1, true), ('Harry Potter', 2, false), ('The Hobbit', 3, false);


insert into books_genres(book_id, genre_id)
values (1, 7),
       (2, 2), (2, 3),
       (3, 2), (3, 3);


insert into comments(text, book_id)
values ('Terrible plot.', 1),
       ('A perfect student for everybody.', 2),
       ('A wonderful fantasy adventure.', 3),
       ('I like the main characters.', 1),
       ('Magical and captivating.', 2),
       ('A perfect blend of magic and adventure.', 3);

insert into users(username, password, age)
values ('user', '$2a$12$jM0niHmbkwkq7qove0rs1Ohu0pees5A7/mqLms/2vAbMlw2WGE.Qa', 17),
       ('admin', '$2a$12$XFFeuNN0c3EEpr1HHAh9L.oFmWz3SaqpI.3xITOCE.ADJAIfg0Tt6', 30),
       ('adult', '$2a$12$W5f4ke6MRMX29oWOjcfi4uu4uhHbD8DWFhR6w8Tcisg88UahfPSU2', 18);

insert into roles(name)
values ('ROLE_USER'),
       ('ROLE_ADMIN'),
       ('ROLE_ADULT_USER');

insert into user_roles(user_id, role_id)
values (1, 1),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 3);

INSERT INTO acl_sid (id, principal, sid) VALUES
    (1, 0, 'ROLE_ADMIN'),
    (2, 0, 'ROLE_USER'),
    (3, 0, 'ROLE_ADULT_USER');

INSERT INTO acl_class (id, class) VALUES
    (1, 'ru.otus.hw.models.Book');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (1, 1, 1, NULL, 1, 0),
       (2, 1, 2, NULL, 1, 0),
       (3, 1, 3, NULL, 1, 0);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
    (1, 1, 0, 3, 1, 1, 1, 1),
    (2, 1, 1, 2, 1, 0, 1, 1),
    (3, 2, 2, 2, 1, 1, 1, 1),
    (4, 2, 3, 1, 31, 1, 1, 1),
    (5, 3, 4, 2, 1, 1, 1, 1),
    (6, 3, 5, 1, 31, 1, 1, 1),
    (7, 2, 6, 3, 1, 1, 1, 1),
    (8, 3, 7, 3, 1, 1, 1, 1),
    (9, 1, 8, 1, 31, 1, 1, 1);

