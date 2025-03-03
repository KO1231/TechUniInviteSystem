/*
 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB.invite_discord TO DBUSER;
 GRANT SELECT, INSERT, CREATE, DELETE REFERENCES, ALTER ON DB.invite_discord_state TO DBUSER;
 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB.invite_discord_joined_user TO DBUSER;

 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB_dev.invite_discord TO DBUSER_dev;
 GRANT SELECT, INSERT, CREATE, DELETE, REFERENCES, ALTER ON DB_dev.invite_discord_state TO DBUSER_dev;
 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB_dev.invite_discord_joined_user TO DBUSER_dev;
 */

INSERT INTO target_app (name) VALUES ('discord');

CREATE TABLE invite_discord (
    invite_id INT PRIMARY KEY,
    guild_id BIGINT UNSIGNED NOT NULL,
    nickname VARCHAR(32) DEFAULT NULL,

    FOREIGN KEY (invite_id) REFERENCES invite(id)
);

CREATE TABLE invite_discord_state (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invite_id INT NOT NULL,
    state VARCHAR(255) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,

    FOREIGN KEY (invite_id) REFERENCES invite_discord(invite_id)
);

CREATE TABLE invite_discord_joined_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invite_id INT NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    joined_at DATETIME NOT NULL,

    FOREIGN KEY (invite_id) REFERENCES invite_discord(invite_id)
);