--  ***************WALLET RELATED TABLES **************
CREATE TABLE wallets_wallet
(
	id bigint NOT NULL AUTO_INCREMENT,
	total_amount decimal(18,2) NOT NULL DEFAULT '0',
	cash_amount decimal(18,2) NOT NULL DEFAULT '0',
	gift_amount decimal(18,2) NOT NULL DEFAULT '0',
	refund_amount decimal(18,2) NOT NULL DEFAULT '0',
	created_on DATETIME NOT NULL,
	modified_on DATETIME NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE users_wallet
(
	id bigint NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	client_id int NOT NULL,
	wallet_id bigint NULL,
	UNIQUE(user_id,client_id),
	UNIQUE(wallet_id),
	CONSTRAINT users_wallet_fk1 FOREIGN KEY (user_id) REFERENCES users_profile(id),
	CONSTRAINT users_wallet_fk2 FOREIGN KEY (client_id) REFERENCES accounts_client(id),
	CONSTRAINT users_wallet_fk3 FOREIGN KEY (wallet_id) REFERENCES wallets_wallet(id),
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE wallets_transaction
(
	id bigint NOT NULL AUTO_INCREMENT,
	transaction_id VARCHAR(100) NOT NULL,
	wallet_id BIGINT NOT NULL,
	amount DECIMAL(18,2) NOT NULL,
	transaction_type VARCHAR(1) NOT NULL,
	transaction_date DATETIME NOT NULL,
	transaction_note longtext NULL,
	CONSTRAINT wallets_transaction_fk1 FOREIGN KEY (wallet_id) REFERENCES wallets_wallet(id),
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

CREATE TABLE wallets_sub_transaction
(
	id bigint NOT NULL AUTO_INCREMENT,
	tran_id bigint NOT NULL,
	transaction_subwallet VARCHAR(50) NOT NULL,
	amount decimal(18,2) NOT NULL,
	order_id integer NULL,
	refund_id integer NULL,
	payment_id integer NULL,
	gift_code VARCHAR(50) NULL,
	gift_expiry DATETIME NULL,
	transaction_reversal_id bigint NULL,
	transaction_description longtext NULL,
	CONSTRAINT wallets_sub_transaction_fk1 FOREIGN KEY (tran_id) REFERENCES wallets_transaction(id),
	CONSTRAINT wallets_sub_transaction_fk2 FOREIGN KEY (order_id) REFERENCES orders_order(id),
	CONSTRAINT wallets_sub_transaction_fk3 FOREIGN KEY (refund_id) REFERENCES payments_refund(id),
	CONSTRAINT wallets_sub_transaction_fk4 FOREIGN KEY (payment_id) REFERENCES payments_paymentattempt(id),
	CONSTRAINT wallets_sub_transaction_fk6 FOREIGN KEY (transaction_reversal_id) REFERENCES wallets_transaction(id),
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--  ***************WALLET RELATED TABLES END**************