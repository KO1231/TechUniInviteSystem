INSERT INTO invite(id, code, search_id, is_disabled, target_app_id, expires_at) VALUES
(1, 'd0f1fcc4759b442184c4d339db4c2c90', 'sample_search_id1', FALSE, 1, NULL),
(2, 'd0f1fcc4759b442184c4d339db4c2c91', 'sample_search_id2', TRUE, 1, NULL);

INSERT INTO invite_discord(id, guild_id) VALUES
(1, 1236697356783587328),
(2, 1236697356783587328);