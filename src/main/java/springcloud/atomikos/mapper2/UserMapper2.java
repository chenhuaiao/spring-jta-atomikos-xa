package springcloud.atomikos.mapper2;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper2 {

    int insert(@Param("name") String name, @Param("age") Integer age);

    Integer insertAccountList(@Param("accountList") List<String> accountList);


    Integer drawAccountMoney(@Param("username") String username, @Param("money") int money);

    Integer addAccountMoney(@Param("username") String username, @Param("money") int money);
}
