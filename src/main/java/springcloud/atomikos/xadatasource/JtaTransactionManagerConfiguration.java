package springcloud.atomikos.xadatasource;

import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@Configuration
@ComponentScan
@EnableTransactionManagement
@Order(-1)
public class JtaTransactionManagerConfiguration {
    // Logger打印日志对象
    private static Logger logger = LoggerFactory.getLogger(JtaTransactionManagerConfiguration.class.getName());

    @Bean(name = "txService", initMethod = "init", destroyMethod = "shutdownWait")
    public com.atomikos.icatch.config.UserTransactionServiceImp userTransactionServiceImp() {
        com.atomikos.icatch.config.UserTransactionServiceImp userTransactionImp = new UserTransactionServiceImp();
        return userTransactionImp;
    }

    @Bean(name = "atomikosUserTransaction")
    @DependsOn("txService")
    public com.atomikos.icatch.jta.UserTransactionImp userTransaction() throws Exception {

        com.atomikos.icatch.jta.UserTransactionImp userTransactionImp =
                new UserTransactionImp();

        userTransactionImp.setTransactionTimeout(300);
        return userTransactionImp;
    }

    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    @DependsOn("txService")
    public com.atomikos.icatch.jta.UserTransactionManager atomikosTransactionManager() throws Exception {

        com.atomikos.icatch.jta.UserTransactionManager transactionManager =
                new UserTransactionManager();
        transactionManager.setForceShutdown(false);

        return transactionManager;
    }

    @Bean(name = "jtaTransactionManager")
    @DependsOn({"txService", "atomikosUserTransaction", "atomikosTransactionManager"})
    @Primary
    public JtaTransactionManager transactionManager(
            @Qualifier("atomikosUserTransaction") UserTransaction userTransaction
            , @Qualifier("atomikosTransactionManager") TransactionManager transactionManager) throws Exception {

        JtaTransactionManager jtaTransactionManager =
                new JtaTransactionManager(userTransaction, transactionManager);

        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }

    @Bean
    public Object testBean(PlatformTransactionManager platformTransactionManager) {
        System.out.println("testBean>>>>>>>>>>" + platformTransactionManager.getClass().getName());
        return new Object();
    }

}
