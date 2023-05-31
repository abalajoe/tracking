create table user_tbl(id bigserial primary key, name varchar(100) not null, email varchar(150) not null unique, password varchar(255) not null, roles varchar(50) not null, dateregistered timestamp default now());

create table product_tbl(id bigserial primary key, name varchar(255) not null, quantity integer not null, cost int not null default 0, recipient varchar(100) not null, deliveryaddress varchar(255) not null, custodian varchar(200) not null, location varchar(200) not null, createddate timestamp without time zone, updateddate timestamp without time zone, CONSTRAINT fk_customer FOREIGN KEY(recipient) REFERENCES user_tbl(email));

create table event_tbl(id bigserial primary key, productfk integer NOT NULL, custodian varchar(150) NOT NULL, location varchar(150) NOT NULL, createddate timestamp without time zone DEFAULT now(), CONSTRAINT fk_product FOREIGN KEY (productfk) REFERENCES product_tbl(id));

insert into user_tbl(name, email, password, roles, dateregistered) values ('admin','admin@gmail.com','$2a$12$6lIb0oFcVyLPwQXNNkUzg.DF6EVQHDvB6.iDpO004pjnHH6peTPhu','ROLE_ADMIN', now());
insert into user_tbl(name,email, password, roles, dateregistered) values ('John Doe','johndoe@gmail.com','$2a$12$re5oIW5jZmgE.OM7X6DswONoHw9qhfM4fbNgOGAY0wEeRbcBGFr4a','ROLE_USER', now());
insert into user_tbl(name,email, password, roles, dateregistered) values ('Mary Moss','marymoss@gmail.com','$2a$12$re5oIW5jZmgE.OM7X6DswONoHw9qhfM4fbNgOGAY0wEeRbcBGFr4a','ROLE_USER', now());

insert into product_tbl(name, quantity, recipient, deliveryaddress, cost, custodian, location, createddate, updateddate) values('Product1',2,'johndoe@gmail.com','Nairobi',3000,'UFS','Kisumu',now(),now());
