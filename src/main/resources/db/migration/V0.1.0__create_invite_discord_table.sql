/*
 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB.invite_discord TO DBUSER;
 GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON DB_dev.invite_discord TO DBUSER_dev;
 */

INSERT INTO target_app (name) VALUES ('discord');

CREATE TABLE invite_discord (
    id INT PRIMARY KEY,
    guild_id BIGINT UNSIGNED NOT NULL,

    FOREIGN KEY (id) REFERENCES invite(id)
);