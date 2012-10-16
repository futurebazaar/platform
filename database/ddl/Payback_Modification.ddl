ALTER TABLE payback_rule_config ADD COLUMN client_name varchar(20) NOT NULL DEFAULT "FUTUREBAZAAR";
ALTER TABLE points_items MODIFY COLUMN earn_ratio decimal(10,4);
ALTER TABLE points_items MODIFY COLUMN burn_ratio decima(10,4);
ALTER TABLE points_items MODIFY COLUMN txn_points decimal(10,4) ;
