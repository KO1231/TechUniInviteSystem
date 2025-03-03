/*
 GRANT LOCK TABLES ON DB.* TO DBUSER;
 GRANT SELECT, INSERT, CREATE, ALTER, REFERENCES ON DB.target_app TO DBUSER;
 GRANT SELECT, INSERT, UPDATE(is_disabled, used), CREATE, REFERENCES, ALTER ON DB.invite TO DBUSER;
 GRANT SELECT, INSERT, UPDATE, CREATE, REFERENCES, ALTER ON DB.flyway_schema_history TO DBUSER;

 GRANT LOCK TABLES ON DB_dev.* TO DBUSER_dev;
 GRANT SELECT, INSERT, CREATE, ALTER, REFERENCES ON DB_dev.target_app TO DBUSER_dev;
 GRANT SELECT, INSERT, UPDATE(is_disabled, used), CREATE, REFERENCES, ALTER ON DB_dev.invite TO DBUSER_dev;
 GRANT SELECT, INSERT, UPDATE, CREATE, REFERENCES, ALTER ON DB_dev.flyway_schema_history TO DBUSER_dev;
 */

CREATE TABLE target_app(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE invite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(36) NOT NULL UNIQUE,
    search_id VARCHAR(255) UNIQUE,
    is_disabled BOOLEAN NOT NULL DEFAULT FALSE,
    is_used BOOLEAN AS (used >= max_used) STORED NOT NULL,
    used INT UNSIGNED NOT NULL DEFAULT 0,
    max_used INT UNSIGNED NOT NULL DEFAULT 1,
    target_app_id INT NOT NULL,
    expires_at DATETIME DEFAULT NULL,

    FOREIGN KEY (target_app_id) REFERENCES target_app(id)
);