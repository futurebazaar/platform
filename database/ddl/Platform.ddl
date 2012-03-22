-- ******************** DROP TABLE AND CONSTRANTS **********


DROP TABLE IF EXISTS categories_store,sso_session, crypto_key,auth_user,users_profile,locations_address ,locations_city,locations_state,locations_country,users_email,users_phone,client_master,
promotion,promotion_uses,promotion_type,promo_values,rules,promotion_bundle_product,promotion_coupon,promotion_user,usage_history,accounts_client;




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


CREATE TABLE auth_user (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(30) NOT NULL,
  first_name varchar(30) NOT NULL,
  last_name varchar(30) NOT NULL,
  email varchar(75) NOT NULL,
  password varchar(128) NOT NULL,
  is_staff tinyint(1) NOT NULL,
  is_active tinyint(1) NOT NULL,
  is_superuser tinyint(1) NOT NULL,
  last_login datetime NOT NULL,
  date_joined datetime NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY username (username)
) ENGINE=InnoDB;

CREATE TABLE users_profile (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  primary_phone varchar(15) NOT NULL,
  secondary_phone varchar(15) NOT NULL,
  buyer_or_seller varchar(100) NOT NULL DEFAULT 'Buyer',
  acquired_through_account_id int(11) DEFAULT NULL,
  full_name varchar(150) NOT NULL,
  primary_email varchar(75) NOT NULL,
  secondary_email varchar(75) DEFAULT NULL,
  gender varchar(1) NOT NULL,
  salutation varchar(15) NOT NULL,
  salt varchar(36) NOT NULL,
  passcode varchar(36) NOT NULL,
  marketing_alerts varchar(25) NOT NULL,
  created_on datetime NOT NULL,
  date_of_birth date DEFAULT NULL,
  is_agent tinyint(1) NOT NULL,
  webpage varchar(200) NOT NULL,
  facebook varchar(200) NOT NULL,
  twitter varchar(200) NOT NULL,
  email_notification tinyint(1) NOT NULL,
  sms_alert tinyint(1) NOT NULL,
  verify_code int(11) DEFAULT NULL,
  profession varchar(200) DEFAULT NULL,
  user_photo varchar(100) DEFAULT NULL,
  atg_username varchar(200) DEFAULT NULL,
  transaction_password varchar(15) DEFAULT NULL,
  atg_login varchar(40) DEFAULT NULL,
  atg_password varchar(35) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY atg_username (atg_username),
  UNIQUE KEY atg_login (atg_login),
  KEY users_profile_403f60f (user_id)
  ) ENGINE=InnoDB;

CREATE TABLE locations_address (
  id int(11) NOT NULL AUTO_INCREMENT,
  pincode varchar(10) NOT NULL,
  city_id int(11) DEFAULT NULL,
  state_id int(11) DEFAULT NULL,
  country_id int(11) DEFAULT NULL,
  type varchar(50) NOT NULL,
  address longtext NOT NULL,
  profile_id int(11) DEFAULT NULL,
  account_id int(11) DEFAULT NULL,
  created_on datetime NOT NULL,
  uses int(10) unsigned NOT NULL,
  name varchar(200) NOT NULL,
  phone varchar(100) NOT NULL,
  email varchar(75) NOT NULL,
  defaddress tinyint(1) NOT NULL,
  first_name varchar(200) NOT NULL,
  last_name varchar(200) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB ;

CREATE TABLE locations_city (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(200) NOT NULL,
  state_id int(11) DEFAULT NULL,
  type varchar(15) NOT NULL,
  normalized_id int(11) DEFAULT NULL,
  user_created tinyint(1) NOT NULL,
  PRIMARY KEY (id)
  ) ENGINE=InnoDB;


CREATE TABLE locations_state (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(200) NOT NULL,
  country_id int(11) NOT NULL,
  type varchar(15) NOT NULL,
  normalized_id int(11) DEFAULT NULL,
  user_created tinyint(1) NOT NULL,
  sap_code varchar(4) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE locations_country (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(200) NOT NULL,
  type varchar(15) NOT NULL,
  normalized_id int(11) DEFAULT NULL,
  user_created tinyint(1) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

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


-- Promotion Related Tables
CREATE TABLE promotion(id INTEGER ,
	applies_on VARCHAR(30), 
	created_by INTEGER, 
	created_on TIMESTAMP,
	valid_from TIMESTAMP,
	valid_till TIMESTAMP, 
	last_modified_on TIMESTAMP, 
	promotion_name VARCHAR(50),
	display_text VARCHAR(300), 
	promotion_description VARCHAR(100),
	last_used_on TIMESTAMP,
	promotion_type INTEGER,
	promotion_uses INTEGER,
	rule_id INTEGER,
	is_coupon INTEGER(1), 
	amount_type INTEGER(1),
	is_active INTEGER(1),
	priority INTEGER(2),
	PRIMARY KEY(id));


CREATE TABLE promotion_uses ( id INTEGER ,
	no_of_uses INTEGER,
	max_per_user INTEGER,
	max_total_usages_allowed INTEGER, 
	PRIMARY KEY(id) );

CREATE TABLE promotion_type ( id INTEGER ,
	type_desc VARCHAR(100), 
	priority INTEGER(2),
	PRIMARY KEY(id) );
-- priority = 1-5

CREATE TABLE promo_values ( id INTEGER ,
	value_name VARCHAR(30), 
	value_desc VARCHAR(50), 
	value_data VARCHAR(500),
	promo_id INTEGER, 
	value_type INTEGER(1), 
	PRIMARY KEY(id) );
-- valueTye = direct/bundle

CREATE TABLE rules ( id INTEGER ,
	rule_desc VARCHAR(100),
	rule_function VARCHAR(50),
	priority INTEGER, 
	PRIMARY KEY(id) );

CREATE TABLE promotion_bundle_product( id INTEGER ,
	promo_bundle_id INTEGER,
	product_id INTEGER, 
	PRIMARY KEY(id) );

CREATE TABLE promotion_coupon(id INTEGER , 
	coupon_code VARCHAR(50),
	promo_id INTEGER,
	PRIMARY KEY(id) );

CREATE TABLE promotion_user( id INTEGER ,
	promo_id INTEGER,
	promo_user_id INTEGER, 
	times_used TIMESTAMP, 
	PRIMARY KEY(id) );

CREATE TABLE usage_history(id INTEGER ,	
	used_on TIMESTAMP,
	used_by INTEGER, 
	promo_id INTEGER, 
	disc_value_claimed INTEGER, 
	PRIMARY KEY(id) );
	
	
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

--  **************  Constraints Users  **********************
ALTER TABLE users_profile ADD CONSTRAINT fk_up_user_id FOREIGN KEY(user_id) REFERENCES auth_user(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE  locations_address  ADD CONSTRAINT fk_la_city_id FOREIGN KEY(city_id) REFERENCES locations_city(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE  locations_address  ADD CONSTRAINT fk_la_state_id FOREIGN KEY(state_id) REFERENCES locations_state(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE  locations_address  ADD CONSTRAINT fk_la_country_id FOREIGN KEY(country_id) REFERENCES locations_country(id) ON UPDATE CASCADE ON DELETE CASCADE;

--  **************  Constraints   **********************
ALTER TABLE promotion ADD CONSTRAINT fk_pr_created_by FOREIGN KEY(created_by) REFERENCES user_profile(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE promotion ADD CONSTRAINT fk_pr_promotion_uses FOREIGN KEY(promotion_uses) REFERENCES promotion_uses(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE promotion ADD CONSTRAINT fk_pr_rule_id FOREIGN KEY(rule_id) REFERENCES rules(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE promo_values ADD CONSTRAINT fk_pv_promo_id FOREIGN KEY(promo_id) REFERENCES promotion(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE promotion_bundle_product ADD CONSTRAINT fk_pbp_product_id FOREIGN KEY(product_id) REFERENCES product(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE promotion_coupon ADD CONSTRAINT fk_pc_promo_id FOREIGN KEY(promo_id) REFERENCES promotion(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE promotion_user ADD CONSTRAINT fk_pu_promo_id FOREIGN KEY(promo_id) REFERENCES promotion(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE promotion_user ADD CONSTRAINT fk_pu_promo_user_id FOREIGN KEY(promo_user_id) REFERENCES user_profile(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE usage_history ADD CONSTRAINT fk_uh_promo_id FOREIGN KEY(promo_id) REFERENCES promotion(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE usage_history ADD CONSTRAINT fk_uh_used_by FOREIGN KEY(used_by) REFERENCES user_profile(id) ON UPDATE CASCADE ON DELETE CASCADE;




--insert into promotion(applies_on,created_on,created_by,valid_from,valid_till,last_modified_on,promotion_name,display_text,promotion_description,last_used_on,promotion_type,promotion_uses,rule_id,is_coupon,amount_type,is_active,priority) 
--values("order",null,null,"1-1-12","3-3-12",null,"try1","try1_disp","try1_desc",null,1,null,1,0,1,1,2);
--insert into promotion(applies_on,created_on,created_by,valid_from,valid_till,last_modified_on,promotion_name,display_text,promotion_description,last_used_on,promotion_type,promotion_uses,rule_id,is_coupon,amount_type,is_active,priority) 
--values("order",null,null,"1-1-12","3-3-12",null,"try2","try2_disp","try2_desc",null,1,null,1,0,1,1,2);


