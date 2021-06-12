DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    userId   varchar(12) NOT NULL,
    password varchar(12) NOT NULL,
    name     varchar(20) NOT NULL,
    email    varchar(50),

    PRIMARY KEY (userId)
);

INSERT INTO USERS
VALUES ('admin', 'password', '자바지기', 'admin@slipp.net');


DROP TABLE IF EXISTS QUESTIONS;

CREATE TABLE QUESTIONS (
    questionId      bigint          auto_increment,
    writer          varchar(30)     not null,
    title           varchar(50)     not null,
    contents        varchar(5000)   not null,
    createdDate     timestamp       not null,
    countOfAnswer   int,
    PRIMARY KEY(questionId)
);


DROP TABLE IF EXISTS ANSWERS;

CREATE TABLE ANSWERS (
   answerId      bigint          auto_increment,
   writer          varchar(30)     not null,
   contents        varchar(5000)   not null,
   createdDate     timestamp       not null,
   questionId      bigint          not null,
   countOfAnswer   int,
   PRIMARY KEY(answerId)
);

