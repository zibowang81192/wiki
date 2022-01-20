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

#电子书表
drop table if exists ebook;
create table ebook(
    id bigint not null comment 'id',
    name varchar(50) comment '名称',
    category1_id bigint comment '分类1',
    category2_id bigint comment '分类2',
    description varchar(200) comment '描述',
    cover varchar(200) comment '封面',
    doc_count int comment '文档数',
    view_count int comment '阅读数',
    vote_count int comment '点赞数',
    primary key (id)
) engine=innodb default charset=utf8mb4 comment='电子书';
insert into ebook (id, name, cover, description) values(1, 'Spring Boot 入门教程', '/image/cover1.png','零基础入门 Java 开发，企业级应用开发最佳首选框架');
insert into ebook (id, name, cover, description) values(2, 'Vue 入门教程','/image/cover2.png','零基础入门 Vue 开发，企业级应用开发最佳首选框架');
insert into ebook (id, name, cover, description) values(3, 'Thinking in Java','/image/cover1.png','Java 入门经典数目');
insert into ebook (id, name, cover, description) values(4, 'Java Core','/image/cover2.png','Java 核心技术，最适合 Java 初学者');
insert into ebook (id, name, cover, description) values(5, '计算机组成与设计: 硬件/软件接口','/image/cover1.png','计算机专业经典书籍，深入理解计算机的组成');

#分类表
drop table if exists category;
create table category(
    id bigint not null comment 'id',
    parent bigint not null default  0 comment '父id',
    name varchar(50) not null comment '名称',
    sort int comment '顺序',
    primary key (id)
) engine=innodb default charset=utf8mb4 comment='分类';
insert into category (id, parent, name, sort) values (100, 000, '前端开发', 100);
insert into category (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into category (id, parent, name, sort) values (102, 100, 'HTML & CSS', 102);
insert into category (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into category (id, parent, name, sort) values (201, 200, '基础应用', 201);
insert into category (id, parent, name, sort) values (202, 200, '框架应用', 202);
insert into category (id, parent, name, sort) values (300, 000, 'Python', 300);
insert into category (id, parent, name, sort) values (301, 300, '基础应用', 301);
insert into category (id, parent, name, sort) values (302, 300, '进阶方向应用', 302);
insert into category (id, parent, name, sort) values (400, 000, '数据库', 400);
insert into category (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into category (id, parent, name, sort) values (500, 000, '其他', 500);
insert into category (id, parent, name, sort) values (501, 500, '服务器', 501);
insert into category (id, parent, name, sort) values (502, 500, '开发工具', 502);
insert into category (id, parent, name, sort) values (503, 500, '热门服务器语言', 503);