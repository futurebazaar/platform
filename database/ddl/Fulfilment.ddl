-- ******************** DROP TABLE AND CONSTRANTS **********
DROP TABLE IF EXISTS fulfillment_sellerpincodemap; 

--  ******************** CREATE TABLE *****************
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
