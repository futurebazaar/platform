package com.fb.api.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface APIMethod {
	String method() default "[get]";
	String urlPattern();
	String action();
}
