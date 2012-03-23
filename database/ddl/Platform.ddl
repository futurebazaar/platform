-- ******************** DROP TABLE AND CONSTRANTS **********


DROP TABLE IF EXISTS categories_store,sso_session, crypto_key,users_profile,locations_city,locations_state,locations_country,users_email,users_phone,client_master,
promotion,coupon,coupon_limits_config,global_coupon_uses,global_promotion_uses,promotion_limits_config,user_coupon_uses,user_promotion_uses,accounts_client;




--  ******************** CREATE TABLE *****************

CREATE TABLE categories_store
(
	id integer NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	slug VARCHAR(50),
	UNIQUE(name),
	PRIMARY KEY(id)
);

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

-- Users Related tables --

CREATE TABLE users_profile (
  id int(11) NOT NULL AUTO_INCREMENT,
  primary_phone varchar(15) NULL,
  secondary_phone varchar(15) NULL,
  buyer_or_seller varchar(100) NOT NULL DEFAULT 'Buyer',
  acquired_through_account_id int(11) DEFAULT NULL,
  password varchar(200) NOT NULL,
  full_name varchar(150) NOT NULL,
  primary_email varchar(75) NOT NULL,
  secondary_email varchar(75) DEFAULT NULL,
  gender varchar(1) NULL,
  salutation varchar(15) NULL,
  marketing_alerts varchar(25) NOT NULL,
  created_on datetime NOT NULL,
  date_of_birth date DEFAULT NULL,
  is_agent tinyint(1) NOT NULL,
  webpage varchar(200) NULL,
  facebook varchar(200) NULL,
  twitter varchar(200) NULL,
  email_notification tinyint(1) NULL,
  sms_alert tinyint(1) NULL,
  profession varchar(200) DEFAULT NULL,
  user_photo varchar(100) DEFAULT NULL,
  PRIMARY KEY (id)
  ) DEFAULT CHARSET=utf8;


CREATE TABLE locations_city (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(200) NOT NULL,
  state_id int(11) DEFAULT NULL,
  type varchar(15) NOT NULL,
  normalized_id int(11) DEFAULT NULL,
  user_created tinyint(1) NOT NULL,
  PRIMARY KEY (id)
  ) DEFAULT CHARSET=utf8;


CREATE TABLE locations_state (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(200) NOT NULL,
  country_id int(11) NOT NULL,
  type varchar(15) NOT NULL,
  normalized_id int(11) DEFAULT NULL,
  user_created tinyint(1) NOT NULL,
  sap_code varchar(4) DEFAULT NULL,
  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE TABLE locations_country (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(200) NOT NULL,
  type varchar(15) NOT NULL,
  normalized_id int(11) DEFAULT NULL,
  user_created tinyint(1) NOT NULL,
  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE TABLE users_email (
  id int(11) NOT NULL AUTO_INCREMENT,
  email varchar(75) NOT NULL,
  type varchar(15) NOT NULL,
  user_id int(11) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY users_email_email_uniq (email)
) DEFAULT CHARSET=utf8;


CREATE TABLE users_phone (
  id int(11) NOT NULL AUTO_INCREMENT,
  phone varchar(15) NOT NULL,
  user_id int(11) NOT NULL,
  type varchar(15) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY users_phone_phone_uniq (phone)
) DEFAULT CHARSET=utf8;


CREATE TABLE client_master (
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  client_name varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  created_on datetime NOT NULL,
  is_active bit(1) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Promotions and Coupons Related Tables

CREATE TABLE promotion (
	id INTEGER NOT NULL AUTO_INCREMENT,
	valid_from DATETIME,
	valid_till DATETIME, 
	name VARCHAR(50),
	description VARCHAR(100),
	is_coupon INTEGER(1),
	is_active INTEGER(1),
	PRIMARY KEY(id)
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
	FOREIGN KEY (promotion_id) REFERENCES promotion(id) ON DELETE CASCADE
);

CREATE TABLE global_promotion_uses (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_id INTEGER,
	current_count INTEGER,
	current_amount DECIMAL(18,2),
	PRIMARY KEY(id),
	UNIQUE(promotion_id),
	FOREIGN KEY (promotion_id) REFERENCES promotion(id) ON DELETE CASCADE
);

CREATE TABLE user_promotion_uses (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_id INTEGER,
	user_id INTEGER,
	current_count INTEGER,
	current_amount DECIMAL(18,2),
	PRIMARY KEY(id),
	UNIQUE(promotion_id),
	FOREIGN KEY (promotion_id) REFERENCES promotion(id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES users_profile(id) ON DELETE CASCADE
);

CREATE TABLE coupon (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_code VARCHAR(30),
	promotion_id INTEGER,
	coupon_type VARCHAR(10),
	PRIMARY KEY(id),
	FOREIGN KEY (promotion_id) REFERENCES promotion(id) ON DELETE CASCADE
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

CREATE TABLE global_coupon_uses (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_id INTEGER,
	current_count INTEGER,
	current_amount DECIMAL(18,2),
	PRIMARY KEY(id),
	UNIQUE(coupon_id),
	FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE CASCADE
);

CREATE TABLE user_coupon_uses (
	id INTEGER NOT NULL AUTO_INCREMENT,
	coupon_id INTEGER,
	user_id INTEGER,
	current_count INTEGER,
	current_amount DECIMAL(18,2),
	PRIMARY KEY(id),
	UNIQUE(coupon_id),
	FOREIGN KEY (coupon_id) REFERENCES coupon(id) ON DELETE CASCADE,
	FOREIGN KEY (user_id) REFERENCES users_profile(id) ON DELETE CASCADE
);

	
CREATE TABLE accounts_client (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  confirmed_order_email varchar(500) NOT NULL DEFAULT '<Chaupaati Bazaar> order@chaupaati.com',
  pending_order_email varchar(500) NOT NULL DEFAULT '<Chaupaati Bazaar> lead@chaupaati.com',
  share_product_email varchar(500) NOT NULL DEFAULT '<Chaupaati Bazaar> share@chaupaati.com',
  signature longtext NOT NULL,
  pending_order_helpline varchar(25) NOT NULL DEFAULT '0-922-222-1947',
  confirmed_order_helpline varchar(25) NOT NULL DEFAULT '0-922-222-1947',
  sms_mask longtext NOT NULL,
  noreply_email varchar(200) NOT NULL,
  feedback_email varchar(200) NOT NULL,
  promotions_email varchar(200) NOT NULL,
  sale_pricelist varchar(15) NOT NULL,
  list_pricelist varchar(15) NOT NULL,
  clientdomain_name varchar(100) DEFAULT NULL,
  slug varchar(100) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


