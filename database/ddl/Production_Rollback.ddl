-- TO Roll back and drop tables from production database created for platform usage

DROP TABLE IF EXISTS sso_session, crypto_key ,promotion_rule_config,promotion_limits_config,global_promotion_uses ,coupon_limits_config ,global_coupon_uses ,coupon ,platform_promotion,promotion_rule;
