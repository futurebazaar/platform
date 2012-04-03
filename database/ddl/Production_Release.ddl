-- Tables for session maintanance

CREATE TABLE sso_session
(
	id integer NOT NULL AUTO_INCREMENT,
	ip_address VARCHAR(25),
	user_id integer NOT NULL,
	timestamp_login DATETIME NOT NULL, 
	timestamp_logout DATETIME,
	timestamp_lastTransaction DATETIME NOT NULL,
	session_id VARCHAR(100) NOT NULL,
	app_data VARCHAR(200),
	UNIQUE(session_id),
	PRIMARY KEY (id)
);

CREATE TABLE crypto_key
(
	id integer NOT NULL AUTO_INCREMENT,
	cryptokey_current VARCHAR(200) NOT NULL,
	cryptokey_last  VARCHAR(200),
	cryptokey_next VARCHAR(200),
	UNIQUE(cryptokey_current),
	PRIMARY KEY (id)
);


-- Promotions and Coupons Related Tables

CREATE TABLE promotion_rule (
	id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(300),
	PRIMARY KEY(id),
	UNIQUE(name)
);

CREATE TABLE platform_promotion (
	id INTEGER NOT NULL AUTO_INCREMENT,
	created_on DATETIME,
	last_modified_on DATETIME,
	rule_id INTEGER,
	valid_from DATETIME,
	valid_till DATETIME, 
	name VARCHAR(50),
	description VARCHAR(100),
	is_coupon INTEGER(1),
	is_active INTEGER(1),
	PRIMARY KEY(id),
	FOREIGN KEY (rule_id) REFERENCES promotion_rule(id)
);

CREATE TABLE promotion_rule_config (
	id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(50),
	value VARCHAR(100),
	promotion_id INTEGER,
	rule_id INTEGER ,
	PRIMARY KEY(id),
	FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id),
	FOREIGN KEY (rule_id) REFERENCES promotion_rule(id)
);

CREATE TABLE promotion_limits_config (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_id INTEGER,
	max_uses INTEGER,
	max_amount DECIMAL(18,2),
	max_uses_per_user INTEGER,
	max_amount_per_user DECIMAL(18,2),
	PRIMARY KEY(id),
	UNIQUE(promotion_id),
	FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id) ON DELETE CASCADE
);

CREATE TABLE user_promotion_uses (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_id INTEGER,
	user_id INTEGER,
	order_id INTEGER,
	discount_amount DECIMAL(18,2),
	PRIMARY KEY(id),
	UNIQUE(promotion_id, order_id, user_id),
	FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES users_profile(id) ON DELETE CASCADE
);

CREATE TABLE coupon (
	id INTEGER NOT NULL AUTO_INCREMENT,
	created_on DATETIME,
	last_modified_on DATETIME,
	coupon_code VARCHAR(30),
	promotion_id INTEGER,
	coupon_type VARCHAR(10),
	PRIMARY KEY(id),
	FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id) ON DELETE CASCADE
);

CREATE TABLE coupon_limits_config (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_id INTEGER,
	max_uses INTEGER,
	max_amount DECIMAL(18,2),
	max_uses_per_user INTEGER,
	max_amount_per_user DECIMAL(18,2),
	PRIMARY KEY(id),
	UNIQUE(coupon_id),
	FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE CASCADE
);

CREATE TABLE user_coupon_uses (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_id INTEGER,
	user_id INTEGER,
	order_id INTEGER,
	discount_amount DECIMAL(18,2),
	PRIMARY KEY(id),
	UNIQUE(coupon_id,user_id,order_id),
	FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES users_profile(id) ON DELETE CASCADE
);

CREATE TABLE coupon_user (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_id int(11) NOT NULL,
	user_id int(11) NOT NULL,
	override_user_uses_limit int(11) NOT NULL,
	PRIMARY KEY(id),
	UNIQUE(coupon_id,user_id),
	FOREIGN KEY (coupon_id) REFERENCES coupon (id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES users_profile (user_id) ON DELETE CASCADE
);


-- end of promotion tables 





