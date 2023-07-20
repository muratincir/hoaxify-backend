package com.hoaxify.ws.user;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.PayloadApplicationEvent;


@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {

	String message() default "{hoaxify.constraint.username.UniqueUsername.message}";

	Class<?>[] groups() default { };

	Class<? extends PayloadApplicationEvent<?>>[] payload() default { };
}
