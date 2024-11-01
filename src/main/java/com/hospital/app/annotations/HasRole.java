package com.hospital.app.annotations;

import com.hospital.app.entities.account.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HasRole {
    Role[] roles() default {};

    boolean justCheckAuthentication() default false;
}
