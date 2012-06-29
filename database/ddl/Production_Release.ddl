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
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE crypto_key
(
	id integer NOT NULL AUTO_INCREMENT,
	cryptokey_current VARCHAR(200) NOT NULL,
	cryptokey_last  VARCHAR(200),
	cryptokey_next VARCHAR(200),
	UNIQUE(cryptokey_current),
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Promotions and Coupons Related Tables

CREATE TABLE promotion_rule (
	id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(300),
	PRIMARY KEY(id),
	UNIQUE(name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE platform_promotion (
	id INTEGER NOT NULL AUTO_INCREMENT,
	created_on DATETIME,
	last_modified_on DATETIME,
	rule_id INTEGER NOT NULL,
	valid_from DATETIME,
	valid_till DATETIME, 
	name VARCHAR(50),
	description VARCHAR(100),
	is_coupon INTEGER(1),
	is_active INTEGER(1),
	PRIMARY KEY(id),
	CONSTRAINT platform_promotion_fk1 FOREIGN KEY (rule_id) REFERENCES promotion_rule(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE promotion_rule_config (
	id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(50),
	value VARCHAR(100),
	promotion_id INTEGER,
	rule_id INTEGER ,
	PRIMARY KEY(id),
	CONSTRAINT promotion_rule_config_fk1 FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id),
	CONSTRAINT promotion_rule_config_fk2 FOREIGN KEY (rule_id) REFERENCES promotion_rule(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE promotion_limits_config (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_id INTEGER,
	max_uses INTEGER NOT NULL,
	max_amount DECIMAL(18,2) NOT NULL,
	max_uses_per_user INTEGER NOT NULL,
	max_amount_per_user DECIMAL(18,2) NOT NULL,
	PRIMARY KEY(id),
	UNIQUE(promotion_id),
	CONSTRAINT promotion_limits_config_fk1 FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_promotion_uses (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_id INTEGER,
	user_id INTEGER,
	order_id INTEGER,
	discount_amount DECIMAL(18,2),
        created_on datetime NOT NULL,
        last_modified_on datetime NOT NULL,
	PRIMARY KEY(id),
	UNIQUE(promotion_id, order_id, user_id),
	CONSTRAINT user_promotion_uses_fk1 FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id) ON DELETE CASCADE,
	CONSTRAINT user_promotion_uses_fk2 FOREIGN KEY (user_id) REFERENCES users_profile(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE released_promotion (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_id INTEGER,
	user_id INTEGER,
	order_id INTEGER,
        created_on datetime NOT NULL,
	discount_amount DECIMAL(18,2),
	PRIMARY KEY(id),
	UNIQUE(created_on),
	CONSTRAINT released_promotion_fk1 FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id) ON DELETE CASCADE,
	CONSTRAINT released_promotion_fk2 FOREIGN KEY (user_id) REFERENCES users_profile(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE coupon (
	id INTEGER NOT NULL AUTO_INCREMENT,
	created_on DATETIME,
	last_modified_on DATETIME,
	coupon_code VARCHAR(30),
	promotion_id INTEGER,
	coupon_type VARCHAR(10),
	PRIMARY KEY(id),
	UNIQUE KEY coupon_code_uniq (coupon_code),
	CONSTRAINT coupon_fk1 FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE released_coupon (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_id INTEGER,
	user_id INTEGER,
	order_id INTEGER,
	discount_amount DECIMAL(18,2),
        created_on datetime NOT NULL,
	PRIMARY KEY(id),
	UNIQUE(created_on),
	CONSTRAINT released_coupon_fk1 FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE CASCADE,
	CONSTRAINT released_coupon_fk2 FOREIGN KEY (user_id) REFERENCES users_profile(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE coupon_limits_config (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_id INTEGER NOT NULL,
	max_uses INTEGER NOT NULL,
	max_amount DECIMAL(18,2) NOT NULL,
	max_uses_per_user INTEGER NOT NULL,
	max_amount_per_user DECIMAL(18,2) NOT NULL,
	PRIMARY KEY(id),
	UNIQUE(coupon_id),
	CONSTRAINT coupon_limits_config_fk1 FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_coupon_uses (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_id INTEGER NOT NULL,
	user_id INTEGER NOT NULL,
	order_id INTEGER NOT NULL,
	discount_amount DECIMAL(18,2),
        created_on datetime NOT NULL,
        last_modified_on datetime NOT NULL,
	PRIMARY KEY(id),
	UNIQUE(coupon_id,user_id,order_id),
	CONSTRAINT user_coupon_uses_fk1 FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE CASCADE,
	CONSTRAINT user_coupon_uses_fk2 FOREIGN KEY (user_id) REFERENCES users_profile(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE platform_coupon_user (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_id INTEGER  NOT NULL,
	user_id INTEGER  NOT NULL,
	override_user_uses_limit INTEGER NOT NULL,
	PRIMARY KEY(id),
	UNIQUE(coupon_id,user_id),
	CONSTRAINT platform_coupon_user_fk1 FOREIGN KEY (coupon_id) REFERENCES coupon (id) ON DELETE CASCADE,
	CONSTRAINT platform_coupon_user_fk2 FOREIGN KEY (user_id) REFERENCES users_profile (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE gift_voucher (
        id int primary key auto_increment,
        number varchar(30) unique key,
        pin varchar(120) not null,
        email varchar(150) not null,
        amount decimal(18,2) not null,
        user_id int not null,
        order_item_id int not null,
        status varchar(80),
        valid_from datetime,
        valid_till datetime,
        created_on datetime,
        last_modified_on datetime
-- Uncomment below 2 lines of code  when Order module will come to platform. Else orders_orderitem table is getting locked during creation on Django front which does not allow platform tnx to proceed. #create web service call
-- CONSTRAINT `gift_voucher_fk1` FOREIGN KEY (`user_id`) REFERENCES `users_profile` (`id`),
--      CONSTRAINT `gift_voucher_fk2` FOREIGN KEY (`order_item_id`) REFERENCES `orders_orderitem` (`id`)
        )ENGINE=InnoDB DEFAULT CHARSET=utf8;

  CREATE TABLE gift_voucher_usage (
        id int primary key auto_increment,
        gift_voucher_number varchar(30) unique key,
        used_by int not null,
        order_id int not null,
        used_on datetime,
        amount_used decimal(18,2) not null,
-- Uncomment below 2 lines of code  when Order module will come to platform. Else orders_order table is getting locked during creation on Django front which does not allow platform tnx to proceed. #use web service call
--       CONSTRAINT `gift_voucher_use_fk1` FOREIGN KEY (`used_by`) REFERENCES `users_profile` (`id`),
--       CONSTRAINT `gift_voucher_use_fk2` FOREIGN KEY (`order_id`) REFERENCES `orders_order` (`id`),
         CONSTRAINT `gift_voucher_use_fk2` FOREIGN KEY (`gift_voucher_number`) REFERENCES `gift_voucher` (`number`)
        )ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE promotions_scratchcard ADD COLUMN user_id INTEGER;

ALTER TABLE promotions_scratchcard ADD CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES users_profile(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE promotions_scratchcard ADD COLUMN used_date DATETIME;
INSERT INTO promotion_rule(id,name,description) VALUES (2,'BUY_WORTH_X_GET_Y_RS_OFF','Buy worth A Rs and get Flat B Rs OFF');
INSERT INTO promotion_rule(id,name,description) VALUES (3,'BUY_WORTH_X_GET_Y_PERCENT_OFF','Buy worth A Rs and get B% Percent OFF');
INSERT INTO promotion_rule(id,name,description) VALUES (1,'BUY_WORTH_X_GET_Y_RS_OFF_ON_Z_CATEGORY','Buy worth A Rs from category B and get C Rs OFF');
INSERT INTO promotion_rule(id,name,description) VALUES (4,'BUY_X_BRAND_GET_Y_RS_OFF_ON_Z_PRODUCT','Buy worth A Rs of brand B products and get C Product for D Rs');

-- end of promotion tables 




