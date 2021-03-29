create table if not exists users
(
    id       bigserial primary key,
    name     varchar(20),
    password varchar(100)
);

create table if not exists roles
(
    id   bigserial primary key,
    name varchar(20)
);

create table if not exists users_roles
(
    user_id bigint
        constraint users_roles_users_id_fk
            references users,

    role_id bigint
        constraint users_roles_roles_id_fk
            references roles
);