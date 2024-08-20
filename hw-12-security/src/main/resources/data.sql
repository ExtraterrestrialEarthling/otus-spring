
insert into authors(full_name)
values ('George Orwell'), ('J.K. Rowling'), ('J.R.R. Tolkien');

insert into genres(name)
values ('Dystopian'), ('Fantasy'), ('Adventure'),
       ('Science Fiction'), ('Historical Fiction'), ('Mystery');

insert into books(title, author_id)
values ('1984', 1), ('Harry Potter', 2), ('The Hobbit', 3);


insert into books_genres(book_id, genre_id)
values (1, 1), (1, 4),
       (2, 2), (2, 3),
       (3, 2), (3, 3);


insert into comments(text, book_id)
values ('A thought-provoking classic.', 1),
       ('A perfect student for everybody.', 2),
       ('A wonderful fantasy adventure.', 3),
       ('Timeless and powerful.', 1),
       ('Magical and captivating.', 2),
       ('A perfect blend of magic and adventure.', 3);

insert into users(username, password)
values ('user', '$2a$12$jM0niHmbkwkq7qove0rs1Ohu0pees5A7/mqLms/2vAbMlw2WGE.Qa');

insert into roles(name)
values ('ROLE_USER');

insert into user_roles(user_id, role_id)
values (1, 1)