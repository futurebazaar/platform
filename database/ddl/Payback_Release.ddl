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
	CONSTRAINT payback_rule_config_fk2 FOREIGN KEY (rule_id) REFERENCES rules(id) ON DELETE CASCADE
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
    PRIMARY KEY (id)
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
    CONSTRAINT points_items_fk2 FOREIGN KEY (points_header_id) REFERENCES points_header(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
