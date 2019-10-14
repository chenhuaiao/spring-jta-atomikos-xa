package springcloud.atomikos.service;

import java.util.List;

public interface AccountMoneyService {
    String drawAccountMoney(Integer money,String username,Integer age) throws InterruptedException, Exception;

    String insertAccount(Integer age);

    List<String> selectUserNameList();

    Integer getDbActiveTest() throws InterruptedException;

    String getUserName();
}
