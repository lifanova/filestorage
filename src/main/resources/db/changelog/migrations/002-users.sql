insert into role (name) values ('admin');
insert into role (name) values ('user');

insert into users (login, password, role_id) values ('admin@mail.ru', '$2a$12$CHtjPozQun/8FW.sx2JCie/a/b5PzpF0Wd3aGr4v35Fsy88jo6qEu', 1);
insert into users (login, password, role_id) values ('user1@mail.ru', '$2a$12$r2X/BWPkWE.T3LAQ4PkTreEMwKZlFnyP50bmuNFAjPZH2tfeLoC7C', 2);