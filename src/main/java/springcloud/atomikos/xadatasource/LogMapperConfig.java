package springcloud.atomikos.xadatasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

//@Configuration
//@MapperScan(basePackages = "springcloud.atomikos.logmapper", sqlSessionTemplateRef = "testLogSqlSessionTemplate")
public class LogMapperConfig {

    @Bean(name = "testLogSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("jtaDataSourceLog") DataSource dataSource)
            throws Exception {

        org.springframework.core.io.Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
                .getResources("classpath:logmapper/*.xml");

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(mapperLocations);

        return bean.getObject();
    }

    @Bean(name = "testLogSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("testLogSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
