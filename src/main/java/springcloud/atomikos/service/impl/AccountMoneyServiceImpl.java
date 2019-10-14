package springcloud.atomikos.service.impl;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springcloud.atomikos.config.SpringContextHolder;
import springcloud.atomikos.mapper1.UserMapper1;
import springcloud.atomikos.mapper2.UserMapper2;
import springcloud.atomikos.service.AccountMoneyService;

import java.util.List;
import java.util.UUID;

@Service
public class AccountMoneyServiceImpl implements AccountMoneyService {
    // Logger打印日志对象
    private static Logger logger = LoggerFactory.getLogger(AccountMoneyServiceImpl.class.getName());

    @Autowired
    private UserMapper1 userMapper1;
    @Autowired
    private UserMapper2 userMapper2;

    @Override
    public String drawAccountMoney(Integer money, String username, Integer age) throws Exception {
        long beginTime = System.currentTimeMillis();

        money = 1;
        int result = userMapper1.drawAccountMoney(username, money);
        /**
         * 检测抛出异常，检测事务是否回滚
         */
        if (age != null && age == 0) {
            throw new RuntimeException();
        }
        userMapper2.addAccountMoney(username, money);

        if (result <= 0) {
            throw new RuntimeException("操作失败，账号余额不足!");
        }

        long endTime = System.currentTimeMillis();

        double seconds = (endTime - beginTime) / 1000.00;
        logger.info("{}{}", username, "接口耗时:[" + seconds + "]秒~");

        return "succ";
    }


    @Override
    public String insertAccount(Integer age) {
        List<String> accountList = Lists.newArrayList();

        for (int i = 0; i < 10; i++) {
            accountList.add(UUID.randomUUID().toString());
        }
        if (age != null && age == 99) {
            accountList.add("chenha");
        }

        Integer result = userMapper1.insertAccountList(accountList);

        if (age != null && age == 0) {
            Object rst = 1 / age;
        }

        Integer result2 = userMapper2.insertAccountList(accountList);

        return "succ:" + result;
    }

    @Override
    public List<String> selectUserNameList() {
        return userMapper1.selectUserNameList();
    }

    @Override
    public Integer getDbActiveTest() throws InterruptedException {

        JtaTransactionManager transactionManager = (JtaTransactionManager) SpringContextHolder
                .getBean("jtaTransactionManager");

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
        def.setReadOnly(true);

        TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态

        Integer result = userMapper1.insertDbActiveTest();

        logger.info(" status.isCompleted():", status.isCompleted());
        transactionManager.commit(status);

        return 1;
    }

    @Override
    public String getUserName() {
        String username = UUID.randomUUID().toString();

        List<String> accountList = Lists.newArrayList(username);
        Integer result = userMapper1.insertAccountList(accountList);
        Integer result2 = userMapper2.insertAccountList(accountList);

        return username;
    }
}
