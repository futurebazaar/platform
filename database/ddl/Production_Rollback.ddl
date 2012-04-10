-- TO Roll back and drop tables from production database created for platform usage

DROP TABLE IF EXISTS sso_session, crypto_key ,user_promotion_uses,user_coupon_uses,promotion_rule_config,promotion_limits_config,coupon_limits_config ,coupon ,platform_promotion,promotion_rule,platform_coupon_user,released_promotion,released_coupon;
