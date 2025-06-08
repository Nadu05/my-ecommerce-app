-- Insert Categories
INSERT INTO categories (name, description) VALUES 
('Electronics', 'Electronic devices and gadgets'),
('Clothing', 'Apparel and fashion items'),
('Books', 'Books and publications'),
('Home & Kitchen', 'Home appliances and kitchen items');

-- Insert Products with additional fields
INSERT INTO products (name, description, price, stock_quantity, image_url, category_id, brand, sku, weight, dimensions) VALUES 
('Smartphone X', 'Latest smartphone with advanced features', 699.99, 50, 'https://res.cloudinary.com/demo/image/upload/v1234567890/smartphone.jpg', 1, 'TechBrand', 'SM-X-001', 0.18, '15 x 7 x 0.8 cm'),
('Laptop Pro', 'High-performance laptop for professionals', 1299.99, 30, '/images/placeholder.png', 1, 'CompTech', 'LP-PRO-002', 2.1, '35 x 25 x 1.8 cm'),
('Wireless Headphones', 'Noise-cancelling wireless headphones', 149.99, 100, '/images/placeholder.png', 1, 'AudioMax', 'WH-003', 0.25, '18 x 15 x 8 cm'),
('Smart Watch', 'Fitness and health tracking smartwatch', 199.99, 75, '/images/placeholder.png', 1, 'TechFit', 'SW-004', 0.05, '4.5 x 3.8 x 1.2 cm'),
('Men''s T-Shirt', 'Comfortable cotton t-shirt for men', 24.99, 200, '/images/placeholder.png', 2, 'FashionWear', 'MTS-005', 0.2, 'M/L/XL'),
('Women''s Jeans', 'Stylish denim jeans for women', 49.99, 150, '/images/placeholder.png', 2, 'DenimStyle', 'WJ-006', 0.5, '28/30/32'),
('Running Shoes', 'Lightweight shoes for running and sports', 89.99, 80, '/images/placeholder.png', 2, 'SportStep', 'RS-007', 0.4, '7/8/9/10'),
('Winter Jacket', 'Warm jacket for cold weather', 129.99, 60, '/images/placeholder.png', 2, 'WinterWear', 'WJ-008', 1.2, 'S/M/L/XL'),
('Science Fiction Novel', 'Bestselling sci-fi novel', 14.99, 100, '/images/placeholder.png', 3, 'BookHouse', 'SFN-009', 0.35, '21 x 14 x 2.5 cm'),
('Cookbook', 'Collection of gourmet recipes', 29.99, 50, '/images/placeholder.png', 3, 'CulinaryPress', 'CB-010', 0.6, '25 x 20 x 3 cm'),
('Coffee Maker', 'Automatic coffee maker for home use', 79.99, 40, '/images/placeholder.png', 4, 'HomeAppliance', 'CM-011', 2.5, '30 x 25 x 40 cm'),
('Blender', 'High-speed blender for smoothies and more', 59.99, 35, '/images/placeholder.png', 4, 'KitchenPro', 'BL-012', 1.8, '20 x 18 x 35 cm');

-- Insert Admin User with plain text password that will be encoded by Spring Security
INSERT INTO users (username, password, email, first_name, last_name, role) VALUES 
('admin', 'admin123', 'admin@example.com', 'Admin', 'User', 'ADMIN');

-- Insert Regular User with plain text password that will be encoded by Spring Security
INSERT INTO users (username, password, email, first_name, last_name, address, phone_number, role) VALUES 
('user', 'user123', 'user@example.com', 'Regular', 'User', '123 Main St, City, Country', '123-456-7890', 'USER');
