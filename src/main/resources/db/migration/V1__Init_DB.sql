create table comment
(
    id          bigint not null,
    color       varchar(255),
    text        varchar(255),
    user_id     bigint,
    comments_id bigint,
    comment_id  bigint,
    primary key (id)
);

create table hibernate_sequence
(
    next_val bigint
);

insert into hibernate_sequence values (1);

create table history
(
    id             bigint not null,
    background_img varchar(300),
    big_text       text,
    title          varchar(255),
    tag_id         bigint,
    primary key (id)
);

create table support_answer
(
    id      bigint not null,
    answer  varchar(1000),
    user_id bigint,
    primary key (id)
);

create table tag
(
    id   bigint not null,
    name varchar(255),
    primary key (id)
);

create table user
(
    id         bigint not null,
    f_active   bit,
    f_age      tinyint,
    f_email    varchar(50),
    f_filename varchar(30),
    f_info     varchar(1000),
    f_password varchar(30),
    f_role     varchar(10),
    f_sex      varchar(10),
    f_telegram varchar(50),
    f_username varchar(30),
    primary key (id)
);

create table user_favorite
(
    users_id    bigint not null,
    favorite_id bigint not null
);

create table user_history
(
    user_id    bigint not null,
    history_id bigint not null
);

create table user_role
(
    user_id bigint not null,
    roles   varchar(255)
);

create table user_support_answer
(
    user_id           bigint not null,
    support_answer_id bigint not null
);


insert into tag
values (1, 'Detective'),
       (2, 'Fantasy'),
       (3, 'Adventures'),
       (4, 'Novel'),
       (5, 'Scientific'),
       (6, 'Humor');

insert into hibernate_sequence values (7);