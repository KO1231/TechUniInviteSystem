/*
 GRANT SELECT, CREATE, REFERENCES, ALTER ON DB.user TO DBUSER;
 GRANT SELECT, CREATE, REFERENCES, ALTER ON DB.user_authority TO DBUSER;
 GRANT SELECT, INSERT, DELETE, UPDATE, CREATE, REFERENCES, ALTER ON DB.flyway_schema_history TO DBUSER;

 GRANT SELECT, INSERT, UPDATE, CREATE, REFERENCES, ALTER ON DB_dev.user TO DBUSER_dev;
 GRANT SELECT, INSERT, UPDATE, CREATE, REFERENCES, ALTER ON DB_dev.user_authority TO DBUSER_dev;
 GRANT SELECT, INSERT, DELETE, UPDATE, CREATE, REFERENCES, ALTER ON DB_dev.flyway_schema_history TO DBUSER_dev;
 */

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL UNIQUE,
    isEnable BOOLEAN NOT NULL,
    name VARCHAR(150) NOT NULL,
    passHash VARCHAR(70) NOT NULL
);

CREATE TABLE user_authority (
    id INT NOT NULL,
    authority VARCHAR(100) NOT NULL,
    PRIMARY KEY (id, authority),
    FOREIGN KEY (id) REFERENCES user(id)
);