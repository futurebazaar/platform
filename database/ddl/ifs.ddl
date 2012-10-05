CREATE TABLE product_weight_volume (
	id INTEGER NOT NULL AUTO_INCREMENT,
	product_id INTEGER NOT NULL,
	actual_weight DECIMAL(10,2),
	length DECIMAL(10,2),
	height DECIMAL(10,2),
	width DECIMAL(10,2),
	PRIMARY KEY(id),
	UNIQUE(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE ifs_product_restrictions (
	id INTEGER NOT NULL AUTO_INCREMENT,
	product_id INTEGER NOT NULL,
	shipping_mode VARCHAR(50),
	lsp VARCHAR(50),
	PRIMARY KEY(id),
	UNIQUE(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
