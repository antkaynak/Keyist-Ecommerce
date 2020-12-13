use keyist;

drop table if exists color;

create table color
(
    id   int auto_increment
        primary key,
    name varchar(50) not null,
    hex  varchar(50) not null,
    constraint color_hex_uindex
        unique (hex),
    constraint color_name_uindex
        unique (name)
);

drop table if exists discount;

create table discount
(
    id               int auto_increment
        primary key,
    code             varchar(240)         not null,
    discount_percent int                  not null,
    status           tinyint(1) default 1 not null,
    constraint code
        unique (code)
);

drop table if exists oauth_access_token;

create table oauth_access_token
(
    token_id          varchar(255) null,
    token             mediumblob   null,
    authentication_id varchar(255) not null
        primary key,
    user_name         varchar(255) null,
    client_id         varchar(255) null,
    authentication    mediumblob   null,
    refresh_token     varchar(255) null
);

drop table if exists oauth_approvals;

create table oauth_approvals
(
    userId         varchar(255)                            null,
    clientId       varchar(255)                            null,
    scope          varchar(255)                            null,
    status         varchar(10)                             null,
    expiresAt      timestamp default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP,
    lastModifiedAt timestamp not null
);

drop table if exists oauth_client_details;

create table oauth_client_details
(
    client_id               varchar(255)  not null
        primary key,
    resource_ids            varchar(255)  null,
    client_secret           varchar(255)  null,
    scope                   varchar(255)  null,
    authorized_grant_types  varchar(255)  null,
    web_server_redirect_uri varchar(255)  null,
    authorities             varchar(255)  null,
    access_token_validity   int           null,
    refresh_token_validity  int           null,
    additional_information  varchar(4096) null,
    autoapprove             varchar(255)  null
);

drop table if exists oauth_client_token;

create table oauth_client_token
(
    token_id          varchar(255) null,
    token             mediumblob   null,
    authentication_id varchar(255) not null
        primary key,
    user_name         varchar(255) null,
    client_id         varchar(255) null
);

drop table if exists oauth_code;

create table oauth_code
(
    code           varchar(255) null,
    authentication mediumblob   null
);

drop table if exists oauth_refresh_token;

create table oauth_refresh_token
(
    token_id       varchar(255) not null,
    token          mediumblob   null,
    authentication mediumblob   null
);

drop table if exists product_category;

create table product_category
(
    id   int auto_increment
        primary key,
    name varchar(50) not null,
    constraint name
        unique (name)
);

drop table if exists product;

create table product
(
    id           int auto_increment
        primary key,
    category_id  int                                  null,
    sku          varchar(50)                          not null,
    name         varchar(100)                         not null,
    url          varchar(100)                         not null,
    long_desc    text                                 not null,
    date_created timestamp  default CURRENT_TIMESTAMP not null,
    last_updated timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    unlimited    tinyint(1) default 1                 null,
    constraint product_ibfk_1
        foreign key (category_id) references product_category (id)
);

create index category_id
    on product (category_id);

drop table if exists product_variant;

create table product_variant
(
    id          int auto_increment
        primary key,
    product_id  int           null,
    color_id    int           not null,
    width       varchar(100)  null,
    height      varchar(100)  null,
    price       float(100, 2) not null,
    composition varchar(259)  null,
    cargo_price float(100, 2) not null,
    tax_percent int default 0 null,
    sell_count  int default 0 null,
    stock       int           not null,
    live        tinyint(1)    not null,
    image       varchar(250)  null,
    thumb       varchar(250)  null,
    constraint color_id
        foreign key (color_id) references color (id),
    constraint product_id
        foreign key (product_id) references product (id)
);

drop table if exists user;

create table user
(
    id                int auto_increment
        primary key,
    email             varchar(500)                         not null,
    password          varchar(500)                         not null,
    first_name        varchar(50)                          null,
    last_name         varchar(50)                          null,
    city              varchar(90)                          null,
    state             varchar(20)                          null,
    zip               varchar(12)                          null,
    email_verified    tinyint(1) default 0                 null,
    registration_date timestamp  default CURRENT_TIMESTAMP null,
    phone             varchar(20)                          null,
    country           varchar(20)                          null,
    address           varchar(100)                         null,
    constraint email
        unique (email)
);

drop table if exists cart;

create table cart
(
    id                int auto_increment
        primary key,
    user_id           int                                 null,
    discount_id       int                                 null,
    total_cart_price  float     default 0                 not null,
    total_cargo_price float     default 0                 not null,
    total_price       float     default 0                 not null,
    date_created      timestamp default CURRENT_TIMESTAMP not null,
    constraint cart_ibfk_1
        foreign key (user_id) references user (id),
    constraint cart_ibfk_2
        foreign key (discount_id) references discount (id)
);

create index discount_id
    on cart (discount_id);

create index user_id
    on cart (user_id);

drop table if exists cart_item;

create table cart_item
(
    id                 int auto_increment
        primary key,
    cart_id            int not null,
    product_variant_id int null,
    amount             int not null,
    constraint cart_item_ibfk_1
        foreign key (cart_id) references cart (id),
    constraint product_variant_id
        foreign key (product_variant_id) references product_variant (id)
);

create index cart_id
    on cart_item (cart_id);

drop table if exists orders;

create table orders
(
    id                int auto_increment
        primary key,
    user_id           int                                  not null,
    ship_name         varchar(100)                         not null,
    ship_address      varchar(100)                         not null,
    billing_address   varchar(100)                         not null,
    city              varchar(50)                          not null,
    state             varchar(50)                          not null,
    zip               varchar(20)                          null,
    country           varchar(50)                          not null,
    phone             varchar(20)                          not null,
    total_price       float                                not null,
    total_cargo_price float                                not null,
    discount_id       int                                  null,
    date              timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    shipped           tinyint(1) default 0                 not null,
    cargo_firm        varchar(100)                         null,
    tracking_number   varchar(80)                          null,
    constraint orders_ibfk_1
        foreign key (user_id) references user (id),
    constraint orders_ibfk_2
        foreign key (discount_id) references discount (id)
);

drop table if exists order_detail;

create table order_detail
(
    id                 int auto_increment
        primary key,
    order_id           int not null,
    product_variant_id int null,
    amount             int not null,
    constraint order_detail_ibfk_1
        foreign key (order_id) references orders (id),
    constraint product_variant_id_ibfk_1
        foreign key (product_variant_id) references product_variant (id)
);

create index order_id
    on order_detail (order_id);

create index discount_id
    on orders (discount_id);

create index user_id
    on orders (user_id);

drop table if exists password_reset_token;

create table password_reset_token
(
    id          int auto_increment
        primary key,
    token       varchar(255)                        not null,
    expiry_date timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    user_id     int                                 not null,
    constraint user_id
        unique (user_id),
    constraint password_reset_token_ibfk_1
        foreign key (user_id) references user (id)
);

drop table if exists verification_token;

create table verification_token
(
    id          int auto_increment
        primary key,
    token       varchar(255)                        not null,
    expiry_date timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    user_id     int                                 not null,
    constraint verification_token_ibfk_1
        foreign key (user_id) references user (id)
);

INSERT INTO keyist.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('test', 'resource-server-rest-api', '$2a$04$v8DNBoc36pw4c7b7Xyq/aeSpGneF9WciZUI9FibVz0neksUcPBXVS', 'read,write', 'password,authorization_code,refresh_token,implicit', null, 'USER', 10800, 2592000, null, null);
INSERT INTO keyist.product_category (id, name) VALUES (1, 'Test');
INSERT INTO keyist.color (id, name, hex) VALUES (1, 'red', '#ff144b');
INSERT INTO keyist.product (id, category_id, sku, name, url, long_desc, date_created, last_updated, unlimited) VALUES (1, 1, '000-0001', 'Test', 'test', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s. ', '2018-05-18 09:50:48', '2020-10-22 01:55:43', 1);
INSERT INTO keyist.product_variant (id, product_id, color_id, width, height, price, composition, cargo_price, tax_percent, sell_count, stock, live, image, thumb) VALUES (1, 1, 1, '4cm', '10cm', 9.99, 'Copper 70%, Zinc 30%', 5, 10, 6, 1000, 1, 'image-url-here', 'image-url-here');