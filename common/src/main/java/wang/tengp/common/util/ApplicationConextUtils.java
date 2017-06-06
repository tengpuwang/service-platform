package wang.tengp.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ApplicationConextUtils implements ApplicationContextAware {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private static ApplicationContext applicationContext;            // Spring应用上下文环境
    private static MessageSourceAccessor messageSourceAccessor;

    public final void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationConextUtils.applicationContext = applicationContext;
        messageSourceAccessor = new MessageSourceAccessor(applicationContext);
    }

    public final static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public final static MessageSourceAccessor getMessageSourceAccessor() {
        return messageSourceAccessor;
    }

    public static Object getBean(String name) {
        if (applicationContext != null) {
            return applicationContext.getBean(name);
        }
        return null;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext != null) {
            return applicationContext.getBean(clazz);
        }
        return null;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        if (applicationContext != null) {
            return applicationContext.getBean(name, clazz);
        }
        return null;
    }

    public static String getPropertiesValue(String key) {
        Properties properties = (Properties) applicationContext.getBean("config");
        String value = properties.getProperty(key);
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        return value.trim();
    }

    public synchronized static void init(String path) {
        if (applicationContext == null) {
            applicationContext = new ClassPathXmlApplicationContext(path);
            messageSourceAccessor = new MessageSourceAccessor(applicationContext);
        }
    }
}