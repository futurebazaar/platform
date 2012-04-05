-- TO Roll back and drop tables from production database created for platform usage

DROP TABLE IF EXISTS sso_session, crypto_key ,promotion_rule_config,promotion_limits_config,coupon_limits_config ,coupon ,platform_promotion,promotion_rule,platform_coupon_user;
