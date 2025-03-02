/*
 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB.invite_discord TO DBUSER;
 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB.invite_discord TO DBUSER;

 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB_dev.invite_discord TO DBUSER_dev;
 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB_dev.invite_discord_state TO DBUSER_dev;
 */

INSERT INTO target_app (name) VALUES ('discord');

CREATE TABLE invite_discord (
    invite_id INT PRIMARY KEY,
    guild_id BIGINT UNSIGNED NOT NULL,

    FOREIGN KEY (invite_id) REFERENCES invite(id)
);

CREATE TABLE invite_discord_state (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invite_id INT NOT NULL,
    state VARCHAR(255) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (invite_id) REFERENCES invite_discord(invite_id)
);