package springcloud.atomikos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {
    // Logger打印日志对象
    private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class.getName());
    
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
        logger.info("SpringContextHolder 加载 Spring 上下文成功!,其他类可以使用 SpringContextHolder.getBean()来获得 bean 对象 ");
    }

    // 获取applicationContext

    public static ApplicationContext getApplicationContext() {

        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        T t = applicationContext.getBean(clazz);
        return t;
    }

    public static Object getBean(String key) {
        return applicationContext.getBean(key);
    }

    // 通过name,以及Clazz返回指定的Bean

    public static <T> T getBean(String name, Class<T> clazz) {

        return getApplicationContext().getBean(name, clazz);

    }
}
