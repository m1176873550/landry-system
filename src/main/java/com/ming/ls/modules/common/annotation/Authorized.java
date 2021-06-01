package com.ming.ls.modules.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorized {
    String[] codes();
}
