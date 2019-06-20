# -- we don't know how to generate root <with-no-name> (class Root) :(
# # create database your_drive;
# # grant all privileges on your_drive.* to 'app_user'@'localhost' identified by 'your_drive';
# # use your_drive;
# use your_drive;
#
# create table hibernate_sequence
# (
#     next_val bigint null
# );
#
# create table user
# (
#     id bigint not null
#         primary key,
#     created_at datetime not null,
#     updated_at datetime not null,
#     email varchar(40) null,
#     password varchar(100) null,
#     constraint UK_t8tbwelrnviudxdaggwr1kd9b
#         unique (email)
# );
#
# create table file_meta
# (
#     id bigint not null
#         primary key,
#     content_type varchar(255) null,
#     created_at date null,
#     path_key varchar(255) null,
#     size bigint null,
#     owner_id bigint null,
#     token varchar(255) not null,
#     constraint file_meta_token_uindex
#         unique (token),
#     constraint FK3ehcrhv0kssvobwwxm9ne0j4p
#         foreign key (owner_id) references user (id)
# );