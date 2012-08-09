CREATE TABLE promotion_config_module (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_id INTEGER NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT promotion_config_module_fk1 FOREIGN KEY (promotion_id) REFERENCES platform_promotion(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE conditions_config (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_config_module_id INTEGER,
	condition_type VARCHAR(30) NOT NULL,
	quantity INTEGER,
	include BOOLEAN,
	product_ids TEXT,
	brand_ids TEXT,
	category_ids TEXT,
	min_order_value DECIMAL(18, 2),
	max_order_value DECIMAL(18, 2),
	join_type VARCHAR(10),
	PRIMARY KEY (id),
	CONSTRAINT conditions_config_fk1 FOREIGN KEY (promotion_config_module_id) REFERENCES promotion_config_module(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE results_config (
	id INTEGER NOT NULL AUTO_INCREMENT,
	promotion_config_module_id INTEGER,
	result_type VARCHAR(30) NOT NULL,
	quantity INTEGER,
	product_ids TEXT,
	brand_ids TEXT,
	category_ids TEXT,
	join_type VARCHAR(10),
	offer_type VARCHAR(30),
	offer_value DECIMAL(18, 2),
	PRIMARY KEY (id),
	CONSTRAINT results_config_fk1 FOREIGN KEY (promotion_config_module_id) REFERENCES promotion_config_module(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE user_promotion_uses ADD COLUMN is_auto_promotion BOOLEAN DEFAULT false;

