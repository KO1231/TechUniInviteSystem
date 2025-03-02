INSERT INTO invite(id, code, search_id, is_disabled, target_app_id, expires_at) VALUES
(1, 'b713ea7a-c273-4112-9183-d9f0de3cecf8', 'sample_search_id1', FALSE, 1, NULL),
(2, '7efd92ff-914e-40e2-8855-748126dadc62', 'sample_search_id2', TRUE, 1, NULL);

INSERT INTO invite_discord(invite_id, guild_id) VALUES
(1, 1236697356783587328),
(2, 1236697356783587328);