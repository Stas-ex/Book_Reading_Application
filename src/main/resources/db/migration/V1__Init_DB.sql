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
insert into hibernate_sequence
values (1);
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
    id                bigint  not null,
    active            bit     not null,
    age               integer not null,
    email             varchar(255),
    img_file          varchar(300),
    info              varchar(1000),
    password          varchar(255),
    sex               varchar(255),
    telegram_username varchar(255),
    username          varchar(255),
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

