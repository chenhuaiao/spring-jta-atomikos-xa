package springcloud.atomikos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import springcloud.atomikos.config.DBConfig1;
import springcloud.atomikos.config.DBConfig2;

@SpringBootApplication(
        scanBasePackages = "springcloud.atomikos"
        , exclude = {
        DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class
        , ActiveMQAutoConfiguration.class, RabbitAutoConfiguration.class
}
)
@ImportResource(value = {
        "classpath:applicatoin-tx.xml"
})
@EnableConfigurationProperties(value = {DBConfig1.class, DBConfig2.class})
public class AtomikosJtaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtomikosJtaApplication.class, args);
    }

}



