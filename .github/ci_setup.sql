CREATE USER 'ci_user'@'localhost' IDENTIFIED BY 'ci_user_pass';
DROP DATABASE IF EXISTS ci; CREATE DATABASE ci;

GRANT LOCK TABLES ON `ci`.* TO `ci_user`@`localhost`;
GRANT SELECT, INSERT, UPDATE, CREATE, REFERENCES, ALTER ON `ci`.`flyway_schema_history` TO `ci_user`@`localhost`;

create table ci.invite(is_disabled INT, used INT);
GRANT SELECT, INSERT, UPDATE (`is_disabled`, `used`), CREATE, REFERENCES, ALTER ON `ci`.`invite` TO `ci_user`@`localhost`;
drop table ci.invite;

GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON `ci`.`target_app` TO `ci_user`@`localhost`;

GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON `ci`.`invite_discord` TO `ci_user`@`localhost`;
GRANT SELECT, INSERT, CREATE, REFERENCES, ALTER ON `ci`.`invite_discord_joined_user` TO `ci_user`@`localhost`;
GRANT SELECT, INSERT, DELETE, CREATE, REFERENCES, ALTER ON `ci`.`invite_discord_state` TO `ci_user`@`localhost`;

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, REFERENCES, ALTER ON `ci`.`user` TO `ci_user`@`localhost`;
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, REFERENCES, ALTER ON `ci`.`user_authority` TO `ci_user`@`localhost`;
