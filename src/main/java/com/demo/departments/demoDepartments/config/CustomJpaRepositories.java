package com.demo.departments.demoDepartments.config;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CustomJpaRepositories {
}
