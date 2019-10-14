package springcloud.atomikos.mapper1;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper1 {

    int insert(@Param("name") String name, @Param("age") Integer age);

    Integer insertAccountList(@Param("accountList") List<String> accountList);

    Integer drawAccountMoney(@Param("username") String username, @Param("money") int money);

    Integer addAccountMoney(@Param("username") String username, @Param("money") int money);

    List<String> selectUserNameList();

    List<Map<String,Object>> selectInfoByUserName(@Param("username") String username);

    Integer insertDbActiveTest();
}
