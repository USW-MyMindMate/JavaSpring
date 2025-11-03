INSERT INTO user_table (id, account, email, password, role, fcm_token)
VALUES (1, 'parentuser', 'parent@test.com', 'pw', 'PARENT', 'TEST_PARENT_TOKEN_9999');

INSERT INTO user_table (id, account, email, password, role, fcm_token, parent_id)
VALUES (2, 'childuser', 'child@test.com', 'pw', 'CHILD', 'TEST_CHILD_TOKEN_8888', 1);
