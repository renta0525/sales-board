-- ユーザー
INSERT INTO users (name, email, password) VALUES
('田中 太郎', 'a', '$2a$10$m06dhaLneiJHuSNcTvA/TeC90AtkGuRALFGYvRjwL908MyePkmpHK'),
('佐藤 花子', 'b', '$2a$10$54o6SxFi9HbSTlx8yEXpSukzkHfWmL.M98SbMFLn/F3XJiVgRFzBa'),
('鈴木 一郎', 'c', '$2a$10$d.b4t8xhcTsLYZHR27UldOAlRZuxK.5WXy/k7.fCLs/XBWusKVQSu');

-- カテゴリー
INSERT INTO categories (name) VALUES
('ドリンク'),
('スイーツ'),
('フード');

-- 商品
INSERT INTO items (name, price, category_id) VALUES
('コーヒー', 300, 1),
('紅茶', 280, 1),
('ショートケーキ', 450, 2),
('ドーナツ', 200, 2),
('ハンバーガー', 600, 3),
('サンドイッチ', 500, 3);

-- 注文
INSERT INTO orders (user_id, order_date, total_price) VALUES
(1, '2025-06-20 10:15:00', 980),
(2, '2025-06-21 14:30:00', 800),
(3, '2025-06-22 18:45:00', 600),
(1, '2025-06-23 12:00:00', 1000);

-- 注文商品（※合計金額と一致しない部分は割引や送料等を想定）
INSERT INTO order_items (order_id, item_id, quantity) VALUES
(1, 1, 2),  -- コーヒー ×2 = 600
(1, 4, 1),  -- ドーナツ ×1 = 200
(1, 2, 1),  -- 紅茶 ×1 = 280

(2, 3, 1),  -- ショートケーキ ×1 = 450
(2, 2, 1),  -- 紅茶 ×1 = 280
(2, 4, 1),  -- ドーナツ ×1 = 200

(3, 5, 1),  -- ハンバーガー ×1 = 600

(4, 6, 2);  -- サンドイッチ ×2 = 1000
