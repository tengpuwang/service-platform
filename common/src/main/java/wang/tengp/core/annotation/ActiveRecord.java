package wang.tengp.core.annotation;

import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

/**
 * ActiveRecord 注解
 * Created by shumin on 16-7-1.
 */
// 定义注解的作用目标
@Target({ElementType.TYPE}) // 接口、类、枚举、注解
// 定义注解的保留策略
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
// 说明该注解将被包含在javadoc中
@Documented
// 说明子类可以继承父类中的该注解
@Inherited
// Spring Repository
@Repository
public @interface ActiveRecord {
}
