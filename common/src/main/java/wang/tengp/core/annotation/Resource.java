package wang.tengp.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * REST Resource
 * Created by shumin on 16/6/16.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Resource {
    String value() default "";
}