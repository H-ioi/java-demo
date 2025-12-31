-- 插入示例数据
INSERT INTO users (username, email, phone_number, user_type) VALUES
('admin', 'admin@example.com', '13800138000', 'ADMIN'),
('user1', 'user1@example.com', '13900139000', 'REGULAR'),
('user2', 'user2@example.com', '13700137000', 'VIP')
ON CONFLICT (username) DO NOTHING;