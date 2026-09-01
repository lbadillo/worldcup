CREATE TABLE team (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    flag VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE stage (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE group_data (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE group_team (
    group_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    PRIMARY KEY (group_id, team_id),
    CONSTRAINT fk_group_team_group
        FOREIGN KEY (group_id) REFERENCES group_data (id),
    CONSTRAINT fk_group_team_team
        FOREIGN KEY (team_id) REFERENCES team (id)
);

CREATE TABLE match_team (
    id BIGINT NOT NULL AUTO_INCREMENT,
    group_id BIGINT,
    stage_id BIGINT NOT NULL,
    team_1_id BIGINT NOT NULL,
    team_2_id BIGINT NOT NULL,
    date_match DATETIME(3) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_match_group
        FOREIGN KEY (group_id) REFERENCES group_data (id),
    CONSTRAINT fk_match_stage
        FOREIGN KEY (stage_id) REFERENCES stage (id),
    CONSTRAINT fk_match_team_1
        FOREIGN KEY (team_1_id) REFERENCES team (id),
    CONSTRAINT fk_match_team_2
        FOREIGN KEY (team_2_id) REFERENCES team (id)
);

CREATE TABLE result (
    id BIGINT NOT NULL AUTO_INCREMENT,
    match_id BIGINT NOT NULL,
    value_1 INT NOT NULL,
    value_2 INT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_result_match (match_id),
    CONSTRAINT fk_result_match
        FOREIGN KEY (match_id) REFERENCES match_team (id)
);

CREATE TABLE user_role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user_tournament (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    provider_user VARCHAR(255) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_tournament_role
        FOREIGN KEY (id) REFERENCES user_role (id)

);

CREATE TABLE bet (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    match_id BIGINT NOT NULL,
    value_1 INT NOT NULL,
    value_2 INT NOT NULL,
    points INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_bet_user
        FOREIGN KEY (user_id) REFERENCES user_tournament (id),
    CONSTRAINT fk_bet_match
        FOREIGN KEY (match_id) REFERENCES match_team (id)
);

