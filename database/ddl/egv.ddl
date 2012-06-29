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
--      CONSTRAINT `gift_voucher_fk2` FOREIGN KEY (`order_item_id`) REFERENCES `orders_orderitem` (`id`)
        )ENGINE=InnoDB DEFAULT CHARSET=utf8;

  CREATE TABLE gift_voucher_usage (
        id int primary key auto_increment,
        gift_voucher_number varchar(30) unique key,
        used_by int not null,
        order_id int not null,
        used_on datetime,
        amount_used decimal(18,2) not null,
-- Uncomment below 2 lines of code  when Order module will come to platform. Else orders_order table is getting locked during creation on Django front which does not allow platform tnx to proceed. #use web service call
--       CONSTRAINT `gift_voucher_use_fk1` FOREIGN KEY (`used_by`) REFERENCES `users_profile` (`id`),
--       CONSTRAINT `gift_voucher_use_fk2` FOREIGN KEY (`order_id`) REFERENCES `orders_order` (`id`),
         CONSTRAINT `gift_voucher_use_fk2` FOREIGN KEY (`gift_voucher_number`) REFERENCES `gift_voucher` (`number`)
        )ENGINE=InnoDB DEFAULT CHARSET=utf8;

