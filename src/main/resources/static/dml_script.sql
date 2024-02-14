USE food_price_monitoring;

INSERT INTO user (username, password, email, user_role) VALUES
-- Hash the password using bcrypt for ADMIN and USERS (https://www.devglan.com/online-tools/bcrypt-hash-generator):
('admin1', '$2a$04$rDQNvZKAlPZNE46eUWIaTuISgdkVmRN0wH7x26DaaWHIs5aqWCLGy', 'admin@example.com', 'ADMIN'), -- Body:('admin1','adminpass1','ADMIN')
('user1', '$2a$04$4683AS1fZ0w9.6xWBB4ct.FRafqHLDvmDmfkg7cgUsfyJ76KEDEra', 'user1@example.com', 'USER'), -- Body:('user1','password1','USER')
('user2', '$2a$04$W1T0dyhmOJpOqGu3C.vnpuzX3MoMRGEewqe9bz4nk2dKd2XIFuXlG', 'user2@example.com', 'USER'), -- Body:('user2','password2','USER')
('marek', '$2a$04$on1naW8dxH3J6VE8ErFjJuPrOovsHnSBpdVPNZztImE8Ivcj/AA5S', 'marekgrzesiak.22@gmail.com', 'USER'); -- Body:('marek','marekg123','USER')

INSERT INTO product_category (category_name) VALUES
('FRUITS'),
('VEGETABLES'),
('DAIRY'),
('MEAT'),
('BEVERAGES');

INSERT INTO trading_point (point_name, address) VALUES
('SuperMart', '123 Main St, New York City, NY 10001'),
('FreshGrocers', '456 Oak Ave, Los Angeles, CA 90001'),
('Organic Haven', '789 Elm Blvd, Chicago, IL 60601');

INSERT INTO product (product_name, category_id) VALUES
('Apple', 1),
('Banana', 1),
('Tomato', 2),
('Cucumber', 2),
('Milk', 3),
('Cheese', 3),
('Beef', 4),
('Chicken', 4),
('Water', 5),
('Soda', 5);

INSERT INTO price_tracking (product_id, point_id, price, start_date, end_date) VALUES
(1, 1, 1.99, '2024-01-13', '2024-01-13'),
(2, 2, 0.99, '2024-01-13', '2024-01-13'),
(3, 1, 2.49, '2024-01-13', '2024-01-13'),
(4, 2, 1.79, '2024-01-13', '2024-01-13'),
(5, 3, 2.29, '2024-01-13', '2024-01-13'),
(6, 3, 3.99, '2024-01-13', '2024-01-13'),
(7, 1, 8.99, '2024-01-13', '2024-01-13'),
(8, 2, 6.49, '2024-01-13', '2024-01-13'),
(9, 3, 0.79, '2024-01-13', '2024-01-13'),
(10, 1, 1.49, '2024-01-13', '2024-01-13');