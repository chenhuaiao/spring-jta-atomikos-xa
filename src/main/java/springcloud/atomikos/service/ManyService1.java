package springcloud.atomikos.service;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import springcloud.atomikos.mapper1.UserMapper1;
import springcloud.atomikos.mapper2.UserMapper2;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class ManyService1 {
    // Logger打印日志对象
    private static Logger logger = LoggerFactory.getLogger(ManyService1.class.getName());

    @Autowired
    private UserMapper1 userMapper1;

    @Autowired
    private UserMapper2 userMapper2;

    /**
     * 对于JMS，
     * 可以使用我们的实例com.atomikos.jms.AtomikosConnectionFactoryBean,
     * com.atomikos.jms.extra.AbstractJmsSenderTemplate（发送信息时使用）
     * com.atomikos.jms.extra.MessageDrivenContainer（接收时使用）
     */
//    @Autowired
//    private JmsTemplate jmsTemplate;


    // 开启事务，由于使用jta+atomikos解决分布式事务，所以此处不必再指定事务
    @Transactional
    public int insert(String name, Integer age) {
        int insert = userMapper1.insert(name, age);
        int i = 1 / age;// 赋值age为0故意引发事务

        return insert;
    }

    // 开启事务，由于使用jta+atomikos解决分布式事务，所以此处不必再指定事务
    @Transactional
    public int insertDb1AndDb2(String name, Integer age) {

        // JMS消息一致性
        send_amq(name);

        name = StringUtils.isEmpty(name) ? UUID.randomUUID().toString() : name;
        age = (age == null) ? 99 : age;

        int insert = userMapper1.insert(name, age);
        int insert2 = userMapper2.insert(name, age);
        int i = 1 / age;// 赋值age为0故意引发事务


        return insert + insert2;
    }

    /**
     * @Description: JMS消息一致性
     * @Param:
     * @return:
     * @Author: huaiao
     * @date: 2019/9/24
     */
    public void send_amq(final String user) {
/*
        jmsTemplate.send("test.queue", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(user);
            }

        });*/

    }

}
