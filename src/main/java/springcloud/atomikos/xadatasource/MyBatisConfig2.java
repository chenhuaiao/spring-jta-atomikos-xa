package springcloud.atomikos.xadatasource;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import springcloud.atomikos.config.DBConfig2;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;


@Configuration
@MapperScan(basePackages = "springcloud.atomikos.mapper2", sqlSessionTemplateRef = "test2SqlSessionTemplate")
public class MyBatisConfig2 {
    // Logger打印日志对象
    private static Logger logger = LoggerFactory.getLogger(MyBatisConfig2.class.getName());

    // 配置数据源
    @Bean(name = "jtaDataSource2")
    @DependsOn("txService")
    public com.atomikos.jdbc.AtomikosDataSourceBean testDataSource(DBConfig2 testConfig) throws SQLException, IOException {
        int minPoolSize = 20;

        Properties properties = new Properties();
        properties.setProperty("url", testConfig.getUrl());
        properties.setProperty("username", testConfig.getUsername());
        properties.setProperty("password", testConfig.getPassword());

        properties.setProperty("initialSize",String.valueOf(minPoolSize));
        properties.setProperty("maxActive", String.valueOf(testConfig.getMaxPoolSize()));
        properties.setProperty("filters","stat,wall");
        properties.setProperty("connectionProperties","druid.stat.mergeSql=true;druid.stat.slowSqlMillis=1000");

        com.atomikos.jdbc.AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setUniqueResourceName("jtaDataSource2");

        xaDataSource.setXaDataSourceClassName(testConfig.getSourceClassName());
        xaDataSource.setXaProperties(properties);

        xaDataSource.setMinPoolSize(minPoolSize);
        xaDataSource.setMaxPoolSize(testConfig.getMaxPoolSize());
        xaDataSource.setTestQuery("SELECT 1");

       /* xaDataSource.setXaDataSourceClassName(druidXADataSource.getClass().getName());
        xaDataSource.setXaDataSource(druidXADataSource);*/

        return xaDataSource;
    }

    @Bean(name = "test2SqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("jtaDataSource2") DataSource dataSource)
            throws Exception {

        org.springframework.core.io.Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper2/*.xml");

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(mapperLocations);

        return bean.getObject();
    }

    @Bean(name = "test2SqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("test2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
