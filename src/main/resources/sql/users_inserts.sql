insert into role (name) values ('admin');
insert into role (name) values ('user');

insert into users (login, password, role_id) values ('admin', 'admin', 1);
insert into users (login, password, role_id) values ('user', 'user', 2);