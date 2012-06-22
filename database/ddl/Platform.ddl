-- ******************** DROP TABLE AND CONSTRANTS **********
DROP TABLE IF EXISTS categories_store; 
DROP TABLE IF EXISTS sso_session;
DROP TABLE IF EXISTS crypto_key;
DROP TABLE IF EXISTS lists_listitem;
DROP TABLE IF EXISTS lists_list;
DROP TABLE IF EXISTS points_items;
DROP TABLE IF EXISTS points_header;
DROP TABLE IF EXISTS orders_orderitem;
DROP TABLE IF EXISTS orders_order;
DROP TABLE IF EXISTS catalog_sellerratechart;
DROP TABLE IF EXISTS platform_coupon_user;
DROP TABLE IF EXISTS orders_order;
DROP TABLE IF EXISTS user_promotion_uses ;
DROP TABLE IF EXISTS user_coupon_uses ;
DROP TABLE IF EXISTS released_coupon;
DROP TABLE IF EXISTS released_promotion;
DROP TABLE IF EXISTS users_email;
DROP TABLE IF EXISTS users_phone ;
DROP TABLE IF EXISTS locations_address ;
DROP TABLE IF EXISTS locations_city;
DROP TABLE IF EXISTS locations_state;
DROP TABLE IF EXISTS locations_country;
DROP TABLE IF EXISTS client_master;
DROP TABLE IF EXISTS promotions_scratchcardcoupons;
DROP TABLE IF EXISTS promotions_scratchcard;
DROP TABLE IF EXISTS promotion_rule_config;
DROP TABLE IF EXISTS promotion_limits_config;
DROP TABLE IF EXISTS coupon_limits_config;
DROP TABLE IF EXISTS coupon ;
DROP TABLE IF EXISTS platform_promotion;
DROP TABLE IF EXISTS promotion_rule;
DROP TABLE IF EXISTS fulfillment_deliverychart_vendor;
DROP TABLE IF EXISTS fulfillment_lspdeliverychart;
DROP TABLE IF EXISTS fulfillment_pincodezipgroupmap;
DROP TABLE IF EXISTS fulfillment_dczipgroup;
DROP TABLE IF EXISTS fulfillment_lspzipgroup;
DROP TABLE IF EXISTS fulfillment_articleproductgroup;
DROP TABLE IF EXISTS fulfillment_lsp;
DROP TABLE IF EXISTS fulfillment_dc;
DROP TABLE IF EXISTS fulfillment_productgroup;
DROP TABLE IF EXISTS inventory_inventory;
DROP TABLE IF EXISTS fulfillment_sellerpincodemap; 
DROP TABLE IF EXISTS accounts_account;
DROP TABLE IF EXISTS accounts_clientdomain;
DROP TABLE IF EXISTS accounts_client;
DROP TABLE IF EXISTS gift_voucher_usage;
DROP TABLE IF EXISTS gift_voucher;
DROP TABLE IF EXISTS users_profile ;
DROP TABLE IF EXISTS auth_user;
DROP TABLE IF EXISTS payback_rule_config;
DROP TABLE IF EXISTS rules;

--  ******************** CREATE TABLE *****************

CREATE TABLE categories_store
(
	id integer NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	slug VARCHAR(50),
	UNIQUE(name),
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE crypto_key
(
	id integer NOT NULL AUTO_INCREMENT,
	cryptokey_current VARCHAR(200) NOT NULL,
	cryptokey_last  VARCHAR(200),
	cryptokey_next VARCHAR(200),
	UNIQUE(cryptokey_current),
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  cod_status varchar(25) NOT NULL DEFAULT 'neutral',
  is_verified tinyint(1) NOT NULL DEFAULT '0',
  verification_code varchar(50) DEFAULT NULL,
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
  cleaned_email varchar(100) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY users_email_email_uniq (email)
) DEFAULT CHARSET=utf8;


CREATE TABLE users_phone (
  id int(11) NOT NULL AUTO_INCREMENT,
  phone varchar(15) NOT NULL,
  user_id int(11) NOT NULL,
  type varchar(15) NOT NULL,
  is_verified tinyint(1) NOT NULL DEFAULT '0',
  verified_on datetime DEFAULT NULL,
  verification_code varchar(50) DEFAULT NULL,
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

CREATE TABLE promotions_scratchcard (
  id INTEGER NOT NULL AUTO_INCREMENT,
  mobile varchar(15) DEFAULT NULL,
  email varchar(100) DEFAULT NULL,
  name varchar(100) DEFAULT NULL,
  scratch_card_no varchar(100) NOT NULL,
  coupon_code varchar(100) DEFAULT NULL,
  status varchar(25) NOT NULL DEFAULT 'active',
  timestamp datetime NOT NULL,
  store varchar(50) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE promotions_scratchcard ADD COLUMN user_id INTEGER;

ALTER TABLE promotions_scratchcard ADD CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES users_profile(id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE promotions_scratchcard ADD COLUMN used_date DATETIME;

CREATE TABLE promotions_scratchcardcoupons (
  id INTEGER NOT NULL AUTO_INCREMENT,
  coupon_code varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  store varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- end of promotion tables 

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


-- IFS tables 
CREATE TABLE `fulfillment_articleproductgroup` (
  `id` int(11) NOT NULL auto_increment,
  `article_id` varchar(16) NOT NULL,
  `product_group_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `fulfillment_articleproductgroup_67dad9bc` (`product_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fulfillment_dc` (
  `id` int(11) NOT NULL auto_increment,
  `code` varchar(6) NOT NULL,
  `name` varchar(100) NOT NULL,
  `cod_flag` tinyint(1) NOT NULL default '0',
  `client_id` int(11) NOT NULL,
  `address` varchar(250) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `fulfillment_dc_code_140b70462a118a7_uniq` (`code`,`client_id`),
  KEY `fulfillment_dc_4a4e8ffb` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fulfillment_dczipgroup` (
  `id` int(11) NOT NULL auto_increment,
  `dc_id` int(11) NOT NULL,
  `zipgroup_id` int(11) NOT NULL,
  `lsp_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `fulfillment_dczipgroup_zipgroup_id_37fd21600b1472ef_uniq` (`zipgroup_id`,`dc_id`,`lsp_id`),
  KEY `fulfillment_dczipgroup_f3c25827` (`dc_id`),
  KEY `fulfillment_dczipgroup_8a9558d2` (`zipgroup_id`),
  KEY `fulfillment_dczipgroup_186fcb02` (`lsp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fulfillment_lsp` (
  `id` int(11) NOT NULL auto_increment,
  `code` varchar(6) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `code` (`code`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fulfillment_lspdeliverychart` (
  `id` int(11) NOT NULL auto_increment,
  `dc_id` int(11) NOT NULL,
  `zipgroup_id` int(11) NOT NULL,
  `transit_time` int(10) unsigned NOT NULL,
  `ship_mode` varchar(8) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `fulfillment_lspdeliverychart_ship_mode_5adee0838f62ccbb_uniq` (`ship_mode`,`zipgroup_id`,`dc_id`),
  KEY `fulfillment_lspdeliverychart_f3c25827` (`dc_id`),
  KEY `fulfillment_lspdeliverychart_8a9558d2` (`zipgroup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fulfillment_lspzipgroup` (
  `id` int(11) NOT NULL auto_increment,
  `lsp_id` int(11) NOT NULL,
  `zipgroup_name` varchar(40) NOT NULL,
  `zipgroup_code` varchar(3) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `fulfillment_lspzipgroup_zipgroup_name_406b3b883ea38d5_uniq` (`zipgroup_name`,`lsp_id`),
  KEY `fulfillment_lspzipgroup_186fcb02` (`lsp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fulfillment_pincodezipgroupmap` (
  `id` int(11) NOT NULL auto_increment,
  `zipgroup_id` int(11) NOT NULL,
  `pincode` varchar(6) NOT NULL,
  `cod_flag` tinyint(1) NOT NULL default '0',
  `high_value` tinyint(1) NOT NULL default '0',
  `supported_product_groups` varchar(100) default NULL,
  `lsp_priority` int(10) unsigned default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `fulfillment_pincodezipgroupmap_zipgroup_id_278c0f82a992ddb_uniq` (`zipgroup_id`,`pincode`),
  KEY `fulfillment_pincodezipgroupmap_8a9558d2` (`zipgroup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fulfillment_productgroup` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(20) NOT NULL,
  `local_tag` tinyint(1) NOT NULL default '0',
  `ship_mode` varchar(8) NOT NULL,
  `high_value_flag` tinyint(1) NOT NULL default '0',
  `threshold_amount` int(10) unsigned default NULL,
  `description` varchar(40) default NULL,
  `client_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `fulfillment_productgroup_name_uniq` (`name`),
  KEY `fulfillment_productgroup_4a4e8ffb` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `inventory_inventory` (
  `id` int(11) NOT NULL auto_increment,
  `rate_chart_id` int(11) NOT NULL,
  `dc_id` int(11) default NULL,
  `type` varchar(25) NOT NULL,
  `stock` decimal(7,2) default NULL,
  `starts_on` datetime default NULL,
  `ends_on` datetime default NULL,
  `expected_on` datetime default NULL,
  `bookings` decimal(7,2) default NULL,
  `outward` decimal(7,2) default NULL,
  `threshold` decimal(7,2) default NULL,
  `stock_adjustment` decimal(7,2) default NULL,
  `bookings_adjustment` decimal(7,2) default NULL,
  `modified_on` datetime default NULL,
  `is_active` tinyint(1) NOT NULL,
  `expected_in` int(10) default NULL,
  PRIMARY KEY  (`id`),
  KEY `rate_chart_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fulfillment_deliverychart_vendor` (
  `pincode` varchar(10) NOT NULL,
  `product_group` varchar(512) NOT NULL,
  `shipping_time` int(11) NOT NULL,
  `delivery_time` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE accounts_clientdomain (
  id int(11) NOT NULL AUTO_INCREMENT,
  client_id int(11) NOT NULL,
  type varchar(25) NOT NULL,
  is_public tinyint(1) NOT NULL,
  custom_home_page varchar(100) NOT NULL,
  domain varchar(150) NOT NULL,
  default_redirect_to varchar(50) NOT NULL,
  code varchar(10) NOT NULL,
  sale_pricelist varchar(15) NOT NULL,
  list_pricelist varchar(15) NOT NULL,
  is_second_factor_auth_reqd tinyint(1) NOT NULL,
  is_channel tinyint(1) NOT NULL,
  PRIMARY KEY (id),
  KEY accounts_clientdomain_4a4e8ffb (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE orders_order (
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) DEFAULT NULL,
  state varchar(25) NOT NULL,
  payable_amount decimal(22,2) NOT NULL,
  timestamp datetime DEFAULT NULL,
  modified_on datetime DEFAULT NULL,
  payment_realized_on datetime DEFAULT NULL,
  payment_mode varchar(25) NOT NULL,
  coupon_discount decimal(22,2) DEFAULT NULL,
  coupon_id int(11) DEFAULT NULL,
  call_id varchar(50),
  list_price_total decimal(22,2) NOT NULL,
  total decimal(22,2) NOT NULL,
  shipping_charges decimal(22,2) NOT NULL,
  taxes decimal(22,2) NOT NULL,
  transaction_charges decimal(22,2) NOT NULL,
  payment_realized_mode varchar(25) NOT NULL,
  medium varchar(20) NOT NULL,
  partner varchar(100) NOT NULL,
  reference_order_id varchar(15) NOT NULL,
  ga_tracked tinyint(1) NOT NULL,
  wstore_id int(11) DEFAULT NULL,
  client_id int(11) NOT NULL,
  top10_discount decimal(22,2) DEFAULT NULL,
  auto_promotions_discount decimal(22,2) DEFAULT NULL,
  verify_code int(11) DEFAULT NULL,
  notes varchar(100) NOT NULL,
  payback_id varchar(16) DEFAULT NULL,
  cashback_amount_total decimal(22,2) DEFAULT NULL,
  booking_agent_id int(11) DEFAULT NULL,
  confirming_agent_id int(11) DEFAULT NULL,
  booking_timestamp datetime DEFAULT NULL,
  confirming_timestamp datetime,
  agent_id int(11) DEFAULT NULL,
  support_state varchar(15) DEFAULT NULL,
  client_domain_id int(11) DEFAULT NULL,
  sap_date datetime DEFAULT NULL,
  PRIMARY KEY (id),
  KEY orders_order_403f60f (user_id),
  KEY orders_order_3d64d1a0 (coupon_id),
  KEY orders_order_388e5db4 (wstore_id),
  KEY orders_order_4a4e8ffb (client_id),
  KEY orders_order_reference_order_id (reference_order_id),
  KEY orders_order_fbe6d70c (payback_id),
  KEY orders_order_2ff4e7ca (booking_agent_id),
  KEY orders_order_61274386 (confirming_agent_id),
  KEY orders_order_c3ec1dbe (agent_id),
  KEY orders_order_timestamp (timestamp),
  KEY orders_order_modified_on (modified_on),
  KEY orders_order_payment_realized_on (payment_realized_on),
  KEY orders_order_support_state (support_state),
  KEY orders_order_state (state),
  KEY orders_order_ef0dcc2f (client_domain_id),
  CONSTRAINT agent_id_refs_id_684bce15b1886510 FOREIGN KEY (agent_id) REFERENCES users_profile (id),
  CONSTRAINT client_domain_id_refs_id_42c094ebad791900 FOREIGN KEY (client_domain_id) REFERENCES accounts_clientdomain (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE rules (
	id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(300),
	PRIMARY KEY(id),
	UNIQUE(name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE payback_rule_config (
	id INTEGER NOT NULL AUTO_INCREMENT,
	name VARCHAR(50),
	value VARCHAR(100),
	rule_id INTEGER ,
	PRIMARY KEY(id),
	CONSTRAINT payback_rule_config_fk2 FOREIGN KEY (rule_id) REFERENCES rules(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE catalog_sellerratechart (
  id int(11) NOT NULL AUTO_INCREMENT,
  sku varchar(100) NOT NULL,
  list_price decimal(10,2) DEFAULT NULL,
  transfer_price decimal(10,2) DEFAULT NULL,
  offer_price decimal(10,2) DEFAULT NULL,
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `lists_list` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `title` varchar(1000) NOT NULL,
    `curator_id` int(11) DEFAULT NULL,
    `description` longtext NOT NULL,
    `banner_image` varchar(100) DEFAULT NULL,
    `starts_on` datetime DEFAULT NULL,
    `ends_on` datetime DEFAULT NULL,
    `is_featured` tinyint(1) NOT NULL DEFAULT '0',
    `type` varchar(50) NOT NULL DEFAULT 'top_10',
    `visibility` varchar(10) NOT NULL,
    `percent_on_10` decimal(22,2) NOT NULL,
    `percent_on_5` decimal(22,2) NOT NULL,
    `detail_page_banner` varchar(100) DEFAULT NULL,
    `detail_page_thumb_banner` varchar(100) DEFAULT NULL,
    `freebies_banner` varchar(100) DEFAULT NULL,
    `home_page_thumb_banner` varchar(100) DEFAULT NULL,
    `tagline` varchar(1000),
    `redirect_to` varchar(300),
    `banner_type` varchar(50) NOT NULL,
    `sort_order` int(10) unsigned,
    `location` varchar(50) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `lists_listitem` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `list_id` int(11) NOT NULL,
    `sku_id` int(11),
    `user_description` longtext DEFAULT NULL,
    `user_title` varchar(1000) DEFAULT NULL,
    `user_image` varchar(100) DEFAULT NULL,
    `status` varchar(10),
    `user_features` longtext,
    `starts_on` datetime DEFAULT NULL,
    `ends_on` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT list_item_fk1 FOREIGN KEY (list_id) REFERENCES lists_list(id),
    CONSTRAINT `sku_id_refs_id_55b7b79d237f0c40` FOREIGN KEY (`sku_id`) REFERENCES `catalog_sellerratechart` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE points_header (
    id INTEGER NOT NULL AUTO_INCREMENT,
    order_id INTEGER NOT NULL,
    reference_id VARCHAR(20) NOT NULL,
    loyalty_card VARCHAR(16) DEFAULT NULL,
    partner_merchant_id VARCHAR(10) NOT NULL,
    partner_terminal_id VARCHAR(10) NOT NULL,
    txn_action_code VARCHAR(50) NOT NULL,
    txn_classification_code VARCHAR(20) NOT NULL,
    txn_payment_type VARCHAR(20) NOT NULL DEFAULT 'OTHERS',
    txn_date DATE NOT NULL,
    settlement_date DATE NOT NULL,
    txn_value INTEGER DEFAULT NULL,
    marketing_code VARCHAR(10) DEFAULT 'DEFAULT',
    branch_id VARCHAR(10) DEFAULT 'ONLINE',
    txn_points INTEGER DEFAULT 0,
    status VARCHAR(10) DEFAULT 'FRESH',
    txn_timestamp DATETIME NOT NULL,
    reason LONGTEXT,
    PRIMARY KEY (id),
    CONSTRAINT points_header_fk1 FOREIGN KEY (order_id) REFERENCES orders_order(id),
    CONSTRAINT points_header_uk1 UNIQUE (order_id, txn_action_code, txn_classification_code, 
        txn_points, txn_value, settlement_date)    
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE orders_orderitem (
    id int(11) NOT NULL AUTO_INCREMENT,
    order_id int(11) NOT NULL,
    qty int(11) NOT NULL DEFAULT '1',
    seller_rate_chart_id int(11) DEFAULT NULL,
    item_title varchar(500) DEFAULT NULL,
    gift_title varchar(500) DEFAULT NULL,
    list_price decimal(22,2) DEFAULT NULL,
    sale_price decimal(22,2) DEFAULT NULL,
    shipping_charges decimal(22,2) DEFAULT 0,
    state varchar(100) DEFAULT NULL,
    created_on DATETIME DEFAULT NULL,
    cashback_amount decimal(22,2) DEFAULT 0,
    is_inventory_blocked tinyint(1) DEFAULT 1,
    dispatched_on datetime DEFAULT NULL,
    dispatch_due_on datetime DEFAULT NULL,
    expected_stock_arrival datetime DEFAULT NULL,
    delivered_on datetime DEFAULT NULL,
    expected_delivery_date datetime DEFAULT NULL,
    modified_on datetime DEFAULT NULL,
    total_amount decimal(22,2) NOT NULL DEFAULT '0.00',
    received_by varchar(100) DEFAULT NULL,
    receivers_contact varchar(15) DEFAULT NULL,
    delivery_days int(11) DEFAULT NULL,
    notes longtext,
    discount decimal(22,2) DEFAULT '0.00',
    PRIMARY KEY (id),
    CONSTRAINT order_items_fk1 FOREIGN KEY (order_id) REFERENCES orders_order(id),
    CONSTRAINT order_items_fk2 FOREIGN KEY (seller_rate_chart_id) REFERENCES catalog_sellerratechart(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE points_items (
    id INTEGER NOT NULL AUTO_INCREMENT,
    points_header_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    department_code INTEGER NOT NULL,
    department_name VARCHAR(50) NOT NULL,
    item_amount INTEGER NOT NULL,
    txn_points DECIMAL(10, 2) DEFAULT 0,
    article_id VARCHAR(20) NOT NULL,
    order_item_id INTEGER NOT NULL,
    earn_ratio DECIMAL(10, 2) DEFAULT '0.03',
    burn_ratio DECIMAL(10, 2) DEFAULT '4',
    PRIMARY KEY (id),
    CONSTRAINT points_items_fk1 FOREIGN KEY (order_item_id) REFERENCES orders_orderitem(id),
    CONSTRAINT points_items_fk2 FOREIGN KEY (points_header_id) REFERENCES points_header(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `accounts_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `website` varchar(200) NOT NULL,
  `type` varchar(100) NOT NULL DEFAULT 'Channel',
  `customer_support_no` varchar(150) NOT NULL,
  `primary_phone` varchar(15) NOT NULL,
  `secondary_phone` varchar(15) NOT NULL,
  `primary_email` varchar(500) NOT NULL,
  `secondary_email` varchar(500) NOT NULL,
  `shipping_policy` longtext NOT NULL,
  `returns_policy` longtext NOT NULL,
  `tos` longtext NOT NULL,
  `code` varchar(200) NOT NULL,
  `dni` varchar(5) NOT NULL,
  `greeting_title` longtext NOT NULL,
  `greeting_text` longtext NOT NULL,
  `is_exclusive` tinyint(1) NOT NULL,
  `confirmed_order_email` varchar(500) NOT NULL,
  `pending_order_email` varchar(500) NOT NULL,
  `signature` longtext NOT NULL,
  `pending_order_helpline` varchar(25) NOT NULL,
  `confirmed_order_helpline` varchar(25) NOT NULL,
  `pg_return_url` varchar(200) NOT NULL,
  `sms_mask` longtext NOT NULL,
  `share_product_email` varchar(500) NOT NULL,
  `client_id` int(11) NOT NULL,
  `slug` varchar(200) NOT NULL,
  `ships_everywhere` tinyint(1) NOT NULL,
  `shipping_duration` int(10) unsigned,
  `delivery_duration` int(10) unsigned,
  `sap_support_needed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `accounts_account_4a4e8ffb` (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;

CREATE TABLE fulfillment_sellerpincodemap (
	id INTEGER NOT NULL AUTO_INCREMENT,
	pincode varchar(6) NOT NULL,
	seller_id int NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT fulfillment_sellerpincodemap_fk1 FOREIGN KEY (seller_id) REFERENCES accounts_account(id),
	CONSTRAINT pincode_seller_key UNIQUE (pincode,seller_id), 
	INDEX (pincode), 
	INDEX (seller_id)
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
--	CONSTRAINT `gift_voucher_fk2` FOREIGN KEY (`order_item_id`) REFERENCES `orders_orderitem` (`id`)
	)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
	
  CREATE TABLE gift_voucher_usage (
	id int primary key auto_increment,
	gift_voucher_number varchar(30) unique key,
	used_by int not null,
	order_id int not null,
	used_on datetime,
	amount_used decimal(18,2) not null,
-- Uncomment below 2 lines of code  when Order module will come to platform. Else orders_order table is getting locked during creation on Django front which does not allow platform tnx to proceed. #use web service call
-- 	 CONSTRAINT `gift_voucher_use_fk1` FOREIGN KEY (`used_by`) REFERENCES `users_profile` (`id`),
--	 CONSTRAINT `gift_voucher_use_fk2` FOREIGN KEY (`order_id`) REFERENCES `orders_order` (`id`),
	 CONSTRAINT `gift_voucher_use_fk2` FOREIGN KEY (`gift_voucher_number`) REFERENCES `gift_voucher` (`number`)
	)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

