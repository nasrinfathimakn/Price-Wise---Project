-- Demo Data for PriceWise Smart Price Comparison Platform

-- 1. Brands
INSERT INTO brands (id, name, description) VALUES
(1, 'Apple', 'Premium consumer electronics, software and online services'),
(2, 'Samsung', 'Global electronics leader offering smartphones, displays, and home appliances'),
(3, 'Sony', 'Japanese multinational entertainment and consumer electronics conglomerate'),
(4, 'HP', 'Personal computing and printing hardware solutions'),
(5, 'boAt', 'Popular consumer audio and lifestyle accessories brand'),
(6, 'OnePlus', 'Consumer electronics including flagship smartphones and earbuds');

-- 2. Categories
INSERT INTO categories (id, name, description, parent_category_id) VALUES
(1, 'Electronics', 'Consumer electronic devices and accessories', NULL),
(2, 'Smartphones', 'Mobile phones and handheld cellular devices', 1),
(3, 'Laptops', 'Portable personal computers and ultrabooks', 1),
(4, 'Audio & Headphones', 'Over-ear headphones, earphones and Bluetooth speakers', 1),
(5, 'Wearables', 'Smartwatches, fitness bands and smart accessories', 1);

-- 3. Retailers
INSERT INTO retailers (id, name, code, website_url, logo_url, active, integration_type) VALUES
(1, 'Amazon India', 'AMAZON_IN', 'https://www.amazon.in', 'https://images.unsplash.com/photo-1523474253243-3834a3e31df1?w=128&q=80', TRUE, 'MOCK'),
(2, 'Flipkart', 'FLIPKART', 'https://www.flipkart.com', 'https://images.unsplash.com/photo-1578916171728-46686eac8d58?w=128&q=80', TRUE, 'MOCK'),
(3, 'Croma', 'CROMA', 'https://www.croma.com', 'https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=128&q=80', TRUE, 'MOCK'),
(4, 'Reliance Digital', 'RELIANCE_DIGITAL', 'https://www.reliancedigital.in', 'https://images.unsplash.com/photo-1526738549149-8e07eca6c147?w=128&q=80', TRUE, 'MOCK');

-- 4. Products (Canonical products with distinct variant configurations)
INSERT INTO products (id, name, brand_id, category_id, model_number, description, image_url, product_identifier, active) VALUES
(1, 'Samsung Galaxy S25 5G (256 GB, Onyx Black)', 2, 2, 'SM-S931B', 'Flagship AI-powered smartphone featuring Dynamic AMOLED 2X, Snapdragon 8 Elite, and 50MP triple camera system.', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&q=80', '8806095000012', TRUE),
(2, 'Samsung Galaxy S25 5G (512 GB, Cobalt Violet)', 2, 2, 'SM-S931B-512', 'Higher storage capacity 512 GB variant with Dynamic AMOLED 2X display and titanium frame.', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&q=80', '8806095000029', TRUE),
(3, 'Apple iPhone 17 (256 GB, Titanium White)', 1, 2, 'A3101', 'Next-generation iPhone with A19 Bionic, ProMotion 120Hz OLED display, and upgraded Ceramic Shield.', 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=600&q=80', '1959490000014', TRUE),
(4, 'HP Pavilion 15 Laptop (Intel Core i5, 16 GB, 512 GB SSD)', 4, 3, '15-eg2000', '15.6-inch FHD anti-glare screen, 13th Gen Intel Core i5 processor, 16GB DDR4 RAM, fast 512GB NVMe SSD.', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&q=80', '1965480000021', TRUE),
(5, 'Sony WH-1000XM5 Wireless Noise Cancelling Headphones', 3, 4, 'WH1000XM5B', 'Industry-leading noise canceling with two processors and 8 microphones, LDAC Hi-Res Audio, 30hr battery.', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80', '0272429230005', TRUE),
(6, 'boAt Stone 650 10W Bluetooth Speaker', 5, 4, 'STONE650', 'Portable rugged wireless speaker with IPX5 water resistance, powerful punchy bass, and 7-hour playback.', 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&q=80', '8904330900034', TRUE);

-- 5. Product Specifications
INSERT INTO product_specifications (id, product_id, attribute_name, attribute_value) VALUES
(1, 1, 'RAM', '12 GB'),
(2, 1, 'Storage', '256 GB'),
(3, 1, 'Display', '6.2-inch Dynamic AMOLED 2X, 120Hz'),
(4, 1, 'Processor', 'Snapdragon 8 Elite'),
(5, 1, 'Rear Camera', '50MP + 12MP + 10MP'),
(6, 1, 'Battery', '4000 mAh'),

(7, 2, 'RAM', '12 GB'),
(8, 2, 'Storage', '512 GB'),
(9, 2, 'Display', '6.2-inch Dynamic AMOLED 2X, 120Hz'),
(10, 2, 'Processor', 'Snapdragon 8 Elite'),

(11, 3, 'Storage', '256 GB'),
(12, 3, 'Display', '6.1-inch Super Retina XDR with ProMotion'),
(13, 3, 'Processor', 'Apple A19 Bionic'),
(14, 3, 'Camera', '48MP Dual Fusion system'),

(15, 4, 'Processor', '13th Gen Intel Core i5-1335U'),
(16, 4, 'RAM', '16 GB DDR4'),
(17, 4, 'Storage', '512 GB PCIe NVMe SSD'),
(18, 4, 'Screen Size', '15.6-inch FHD (1920x1080) IPS'),

(19, 5, 'Type', 'Over-Ear Wireless'),
(20, 5, 'Battery Life', '30 Hours'),
(21, 5, 'Noise Cancellation', 'Active Noise Cancelling (Auto NC Optimizer)'),
(22, 5, 'Connectivity', 'Bluetooth 5.2 & 3.5mm Aux'),

(23, 6, 'Power Output', '10W RMS'),
(24, 6, 'Water Resistance', 'IPX5 Dust & Splash Proof'),
(25, 6, 'Battery Life', 'Up to 7 Hours');

-- 6. Product Offers (Realistic multi-retailer pricing with known/unknown delivery charges)
-- Samsung Galaxy S25 256GB offers
INSERT INTO product_offers (id, product_id, retailer_id, external_product_id, external_title, product_url, image_url, current_price, original_price, currency, delivery_charge, availability_status, last_fetched_at, last_successful_fetch_at, active) VALUES
(1, 1, 1, 'AMZ-B0CS925X', 'Samsung Galaxy S25 5G (Onyx Black, 256GB Storage) (12GB RAM)', 'https://www.amazon.in/dp/B0CS925X', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&q=80', 74999.00, 79999.00, 'INR', 0.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(2, 1, 2, 'FLIP-MOB-S25-256', 'SAMSUNG Galaxy S25 5G (Onyx Black, 256 GB) (12 GB RAM)', 'https://www.flipkart.com/samsung-galaxy-s25-5g-onyx-black-256-gb/p/itms2501', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&q=80', 73999.00, 79999.00, 'INR', 99.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(3, 1, 3, 'CROMA-265100', 'Samsung Galaxy S25 5G 256GB Black Smartphone', 'https://www.croma.com/p/265100', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&q=80', 75490.00, 79999.00, 'INR', NULL, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),

-- Samsung Galaxy S25 512GB offers
(4, 2, 1, 'AMZ-B0CS925X-512', 'Samsung Galaxy S25 5G (Cobalt Violet, 512GB Storage) (12GB RAM)', 'https://www.amazon.in/dp/B0CS925X512', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&q=80', 84999.00, 89999.00, 'INR', 0.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(5, 2, 2, 'FLIP-MOB-S25-512', 'SAMSUNG Galaxy S25 5G (Cobalt Violet, 512 GB) (12 GB RAM)', 'https://www.flipkart.com/samsung-galaxy-s25-5g-violet-512-gb/p/itms2502', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&q=80', 85499.00, 89999.00, 'INR', 0.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),

-- iPhone 17 offers
(6, 3, 1, 'AMZ-B0IP17-256', 'Apple iPhone 17 (256 GB) - Titanium White', 'https://www.amazon.in/dp/B0IP17256', 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=600&q=80', 89900.00, 89900.00, 'INR', 0.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(7, 3, 2, 'FLIP-IP17-256', 'Apple iPhone 17 (Titanium White, 256 GB)', 'https://www.flipkart.com/apple-iphone-17-titanium-white-256-gb/p/itmip17', 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=600&q=80', 88990.00, 89900.00, 'INR', 99.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(8, 3, 4, 'REL-IP17-256', 'Apple iPhone 17 256 GB, Titanium White', 'https://www.reliancedigital.in/p/apple-iphone-17-256gb', 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=600&q=80', 89490.00, 89900.00, 'INR', NULL, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),

-- HP Pavilion 15 offers
(9, 4, 1, 'AMZ-B0HP-PAV15', 'HP Pavilion 15 13th Gen Intel Core i5 15.6 inch FHD Laptop (16GB RAM/512GB SSD)', 'https://www.amazon.in/dp/B0HPPAV15', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&q=80', 64990.00, 72500.00, 'INR', 0.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(10, 4, 2, 'FLIP-HP-PAV15', 'HP Pavilion Core i5 13th Gen - (16 GB/512 GB SSD/Windows 11 Home) 15-eg2000', 'https://www.flipkart.com/hp-pavilion-intel-core-i5-13th-gen/p/itmhp15', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&q=80', 63490.00, 72500.00, 'INR', 149.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),

-- Sony WH-1000XM5 offers
(11, 5, 1, 'AMZ-B09XS-XM5', 'Sony WH-1000XM5 Wireless Active Noise Cancelling Headphones - Black', 'https://www.amazon.in/dp/B09XSXM5', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80', 26990.00, 34990.00, 'INR', 0.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(12, 5, 2, 'FLIP-SONY-XM5', 'SONY WH-1000XM5 Bluetooth Headset (Black, On the Ear)', 'https://www.flipkart.com/sony-wh-1000xm5-bluetooth-headset/p/itmsonyxm5', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80', 27490.00, 34990.00, 'INR', 70.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(13, 5, 3, 'CROMA-SONY-XM5', 'Sony WH-1000XM5 Over Ear Active Noise Cancellation Headphones', 'https://www.croma.com/sony-wh-1000xm5/p/260021', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80', 26490.00, 34990.00, 'INR', 0.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),

-- boAt Stone 650 offers
(14, 6, 1, 'AMZ-B07X-ST650', 'boAt Stone 650 10W Bluetooth Speaker with IPX5 and 7H Playtime (Black)', 'https://www.amazon.in/dp/B07XST650', 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&q=80', 1799.00, 4990.00, 'INR', 40.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
(15, 6, 2, 'FLIP-BOAT-ST650', 'boAt Stone 650 10 W Bluetooth Speaker (Black)', 'https://www.flipkart.com/boat-stone-650-10-w-bluetooth-speaker/p/itmboat650', 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=600&q=80', 1699.00, 4990.00, 'INR', 0.00, 'IN_STOCK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE);

-- 7. Price History Snapshots (Realistic 30-day timeline observations for chart rendering)
INSERT INTO price_history (id, product_offer_id, price, delivery_charge, currency, availability_status, observed_at, data_source) VALUES
(1, 1, 79999.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '25' DAY, 'MOCK'),
(2, 1, 77999.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '18' DAY, 'MOCK'),
(3, 1, 76499.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '10' DAY, 'MOCK'),
(4, 1, 74999.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP, 'MOCK'),

(5, 2, 79999.00, 99.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '25' DAY, 'MOCK'),
(6, 2, 75999.00, 99.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '15' DAY, 'MOCK'),
(7, 2, 73999.00, 99.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP, 'MOCK'),

(8, 11, 29990.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '30' DAY, 'MOCK'),
(9, 11, 28490.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '20' DAY, 'MOCK'),
(10, 11, 27990.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '10' DAY, 'MOCK'),
(11, 11, 26990.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP, 'MOCK'),

(12, 13, 29990.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '28' DAY, 'MOCK'),
(13, 13, 27490.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP - INTERVAL '14' DAY, 'MOCK'),
(14, 13, 26490.00, 0.00, 'INR', 'IN_STOCK', CURRENT_TIMESTAMP, 'MOCK');

-- 8. Users (Admin & Demo User with BCrypt hash for Password@123)
-- BCrypt for 'Password@123': $2a$10$wN105q3U9N72Jc58GzI/2eG4tCekcZ2sI6x7E1cQkH798GzWnJj.y
INSERT INTO users (id, name, email, password_hash, role, enabled) VALUES
(1, 'Administrator', 'admin@pricewise.local', '$2a$10$wN105q3U9N72Jc58GzI/2eG4tCekcZ2sI6x7E1cQkH798GzWnJj.y', 'ADMIN', TRUE),
(2, 'John Doe', 'john.doe@example.com', '$2a$10$wN105q3U9N72Jc58GzI/2eG4tCekcZ2sI6x7E1cQkH798GzWnJj.y', 'USER', TRUE);

-- 9. Watchlist & Price Alerts
INSERT INTO watchlist_items (id, user_id, product_id) VALUES
(1, 2, 1),
(2, 2, 5);

INSERT INTO price_alerts (id, user_id, product_id, target_price, currency, active) VALUES
(1, 2, 1, 74000.00, 'INR', TRUE),
(2, 2, 5, 25000.00, 'INR', TRUE);

-- 10. Sample Price Update Logs
INSERT INTO price_update_logs (id, retailer_id, started_at, completed_at, status, offers_processed, error_message) VALUES
(1, 1, CURRENT_TIMESTAMP - INTERVAL '2' HOUR, CURRENT_TIMESTAMP - INTERVAL '2' HOUR + INTERVAL '4' SECOND, 'SUCCESS', 5, NULL),
(2, 2, CURRENT_TIMESTAMP - INTERVAL '2' HOUR, CURRENT_TIMESTAMP - INTERVAL '2' HOUR + INTERVAL '5' SECOND, 'SUCCESS', 5, NULL),
(3, 3, CURRENT_TIMESTAMP - INTERVAL '2' HOUR, CURRENT_TIMESTAMP - INTERVAL '2' HOUR + INTERVAL '3' SECOND, 'SUCCESS', 2, NULL);

