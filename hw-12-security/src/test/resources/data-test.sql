insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

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
values (1, 1)