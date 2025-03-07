INSERT INTO user(id, uuid, isEnable, name, passHash) VALUES
(1, 'd5913dee-3d46-4928-aab2-cec9ba00776b', true, 'testUser001', '$2b$16$g4F3vg7maj1SPoghtCEYE.rY3/B9Yn1Zbyg7L.1EgyHpqFpfbKP6a' /* o*h3JvFO%m */ /* 最小文字数(10) */),
(2, 'e5bc1a3b-13a0-4426-a4e3-93851142c8dc', true, 'readUser001', '$2b$16$ov5U9hf4ynVk2xaWXasrUuXwP.XysrKuhzaZH73Uf5a4vVfrFqMSe' /* 2gugqSU#bB?4kJTy!xu23nyV7GPs6rw%-Od5wf#ySP85cZrUOFb-nj!BHvt7w$oz */ /* 最大文字数(64) */),
(3, '8c583f43-32f1-4120-ad07-a4d73ff0596a', true, 'writeUser001', '$2b$16$32a90qZWcTZ1LuAejuYgC.vu1ntjfN5ajypzu2cXNx/NIwKdKuHZO' /* LsQ$N*zsvdB9Dk5QUsw% */),
(4, 'ff88ed84-de7a-4cda-b31b-b5378bb3d0d5', true, 'adminUser001', '$2b$16$NQ23hg4LnV1BDW9OfyzCze9UeBImkkrwV2nnuHAlbhgaL6AHNpx8S' /* 2gugqSU?bBl4kJTy!xu23nyV7GPs6rw%t#B2M!i&Zj#1vQ&rgsl3sI#ZsTSvS-$G */ /* 最大文字数(64) */),
(5, 'edf39014-16ba-49bb-abc7-9bf8697c19a0', false, 'disabledUser001', '$2b$16$YoL7kucl7v9OElFryDYj1.dOOg9VmFMRGTs9jwMgnyfQbSyT.25.u' /* disabled#PASS001 */);

INSERT INTO user_authority(id, authority) VALUES
(3, 'INVITE_WRITE'),
(4, 'INVITE_WRITE');
