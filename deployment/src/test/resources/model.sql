DROP ALL OBJECTS;

CREATE TABLE MESSAGE(
    ID INTEGER NOT NULL PRIMARY KEY,
    TEXT VARCHAR(20),
    LANGUAGE VARCHAR(5),
    COUNTRY VARCHAR(5)
);

insert into MESSAGE (id, text, language, country) values (1, 'hello', 'en', 'US');
insert into MESSAGE (id, text, language, country) values (2, '世界', 'ja', 'JP');
