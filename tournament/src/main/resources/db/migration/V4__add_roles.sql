INSERT INTO user_role values (1, 'admin');
INSERT INTO user_role values (2, 'user');
INSERT INTO user_role values (3, 'manager');

ALTER TABLE user_tournament ADD user_role_id BIGINT DEFAULT 2 ;
ALTER TABLE user_tournament ADD CONSTRAINT user_tournament_role_FK FOREIGN KEY (user_role_id) REFERENCES user_role(id);

