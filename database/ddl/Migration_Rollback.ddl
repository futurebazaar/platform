-- TO Roll back and delete data from production database created for platform migration

DELETE FROM user_promotion_uses;
DELETE FROM user_coupon_uses;
DELETE FROM promotion_rule_config;
DELETE FROM promotion_limits_config;
DELETE FROM coupon_limits_config ;
DELETE FROM platform_coupon_user;
DELETE FROM coupon;
DELETE FROM platform_promotion;
