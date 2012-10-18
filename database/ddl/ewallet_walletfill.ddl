CREATE TABLE wallets_fill_xml
(
	id bigint NOT NULL AUTO_INCREMENT,
	wallet_id bigint NOT NULL,
	user_id bigint NOT NULL,
	payment_mode VARCHAR(10) NOT NULL,
	amount decimal(18,2) NOT NULL,
	wallet_fill_xml longtext NOT NULL,
	created_date DATETIME NOT NULL,
	status bit NOT NULL DEFAULT 0,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;