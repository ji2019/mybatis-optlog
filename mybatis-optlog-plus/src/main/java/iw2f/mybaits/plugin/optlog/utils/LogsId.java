package iw2f.mybaits.plugin.optlog.utils;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface LogsId {
    /**
     * 字段名（该值可无）
     */
    String value() default "";
}