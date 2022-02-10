/* Productos */
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Zumo de Naranja', 9.5, 25, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Ternera', 17.50, 10, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Vino', 11.25, 15, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Pan', 1.50, 20, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Queso', 7.50, 18, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Atun', 5.80, 8, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Tomates', 6.50, 15, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Coca Cola', 0.75, 50, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Arroz', 1, 15, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Lechuga', 3.00, 10, NOW(), 'https://api.lorem.space/image?w=150&h=180');
insert into PRODUCTO (id, nombre, precio, stock, created_at, imagen)
values (NEXTVAL('hibernate_sequence'), 'Cerveza', 0.50, 50, NOW(), 'https://api.lorem.space/image?w=150&h=180');

-- Contraseña: Admin1
insert into usuarios (id, full_name, email, username, password, avatar, created_at, last_password_change_at)
values (1, 'Admin admin', 'admin@prueba.net', 'admin', '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2',
        'https://api.lorem.space/image/face?w=150&h=150', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into usuario_roles (usuario_id, roles)
values (1, 'USER');
insert into usuario_roles (usuario_id, roles)
values (1, 'ADMIN');

-- Contraseña: Marialopez1
insert into usuarios (id, full_name, email, username, password, avatar, created_at, last_password_change_at)
values (2, 'María López', 'maria.lopez@prueba.net', 'marialopez',
        '$2a$10$3i95KIxdl8igcpDby.URMOgwdDR2q9UaSfor2kJJrhAPfNOC/HMSi',
        'https://api.lorem.space/image/face?w=150&h=150', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into usuario_roles (usuario_id, roles)
values (2, 'USER');

-- Contraseña: Angelmartinez1
insert into usuarios (id, full_name, email, username, password, avatar, created_at, last_password_change_at)
values (3, 'Ángel Martínez', 'angel.martinez@prueba.net', 'angelmartinez',
        '$2a$10$37IEM6zzuwXqFrotYDtySOKITKfeNWR3NBRqcM7JYWnBDIaq9ByZm',
        'https://api.lorem.space/image/face?w=150&h=150', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

insert into usuario_roles (usuario_id, roles)
values (3, 'USER');

-- Contraseña: Anajimenez1
insert into usuarios (id, full_name, email, username, password, avatar, created_at, last_password_change_at)
values (4, 'Ana Jiménez', 'ana.jimenez@prueba.net', 'anajimenez',
        '$2a$10$k0om5gtNBheWX54VzD1E0etCnqC0xChHjfW3lOXaeCpN5ST1vVGYm',
        'https://api.lorem.space/image/face?w=150&h=150', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into usuario_roles (usuario_id, roles)
values (4, 'USER');

