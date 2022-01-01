#建立test表
drop table if exists test;
create table test(
                     id bigint not null comment 'id',
                     name varchar(50) comment '名称',
                     password varchar(50) comment '密码',
                     primary key (id)
) engine=innodb default charset=utf8mb4 comment='测试';
INSERT INTO test (id, name, password) VALUES (1, '史蒂芬周', '9527');

#建立demo表
drop table if exists demo;
create table demo(
                     id bigint not null comment 'id',
                     name varchar(50) comment '名称',
                     primary key (id)
) engine=innodb default charset=utf8mb4 comment='测试';
INSERT INTO demo (id, name) VALUES (1, '史蒂芬周');