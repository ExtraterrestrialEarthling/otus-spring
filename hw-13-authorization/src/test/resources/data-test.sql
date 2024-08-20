insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id, adult_only)
values ('BookTitle_1', 1, false), ('BookTitle_2', 2, false), ('BookTitle_3', 3, false);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(text, book_id)
values ('nice', 1), ('cool', 1), ('good', 2), ('interesting', 3);

insert into users(username, password)
values ('user', '$2a$12$jM0niHmbkwkq7qove0rs1Ohu0pees5A7/mqLms/2vAbMlw2WGE.Qa');

insert into roles(name)
values ('ROLE_USER');

insert into user_roles(user_id, role_id)
values (1, 1);

    INSERT INTO acl_sid (id, principal, sid) VALUES
    (1, 0, 'ROLE_USER');

INSERT INTO acl_class (id, class) VALUES
    (1, 'ru.otus.hw.models.Book');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES (1, 1, 1, NULL, 1, 0),
       (2, 1, 2, NULL, 1, 0),
       (3, 1, 3, NULL, 1, 0);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
       (1, 1, 0, 1, 1, 1, 1, 1),
       (2, 2, 1, 1, 1, 1, 1, 1),
       (3, 3, 2, 1, 1, 1, 1, 1);

