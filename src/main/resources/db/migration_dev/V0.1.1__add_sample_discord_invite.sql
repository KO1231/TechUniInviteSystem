INSERT INTO invite(id, code, search_id, is_disabled, used, max_used, target_app_id, expires_at) VALUES
(1, 'b713ea7a-c273-4112-9183-d9f0de3cecf8', 'sample_search_id_normal', FALSE, 0, 1, 1, NULL),
(2, '7efd92ff-914e-40e2-8855-748126dadc62', 'sample_search_id_disabled', TRUE, 0, 5, 1, NULL),
(3, '06ad45fc-e447-4f12-819d-cf7e5ee0fbca', 'sample_search_id_many_max_used', FALSE, 2, 70, 1, NULL),
(4, 'f04a652d-c5e3-480f-89f4-e6235e2038e4', 'sample_search_id_expired', FALSE, 0, 1, 1, '2020-10-26 12:34:56'),
(5, 'bfb7db33-1444-4c99-96c8-2e76e74b1c1a', 'sample_search_id_normal2', FALSE, 0, 1, 1, NULL),
(6, 'ef22d323-cc83-4346-a5ce-bb11272a68ea', 'sample_search_id_long_term', FALSE, 0, 100, 1, '2100-10-26 12:34:56');


INSERT INTO invite_discord(invite_id, guild_id, nickname) VALUES
(1, 1236697356783587328, NULL),
(2, 1236697356783587328, 'disable'),
(3, 1236697356783587328, 'many_max_use USER'),
(4, 1236697356783587328, 'expired'),
(5, 1236697356783587328, 'テスト太郎 123'),
(6, 1236697356783587328, 'long_term USER');