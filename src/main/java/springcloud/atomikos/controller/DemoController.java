package springcloud.atomikos.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springcloud.atomikos.service.AccountMoneyService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DemoController {
    // Logger打印日志对象
    private static Logger logger = LoggerFactory.getLogger(DemoController.class.getName());

    @Autowired
    private AccountMoneyService accountMoneyService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }


    @GetMapping("/username")
    public JSONObject getUserName() {
        String username = accountMoneyService.getUserName();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        return jsonObject;
    }

    @GetMapping("draw")
    public String drawMoney(HttpServletRequest request
            , @RequestParam(name = "username") String username
            , @RequestParam(name = "money", required = false) Integer money
            , @RequestParam(name = "age", required = false) Integer age
    ) throws Exception {

        try {
            logger.info("draw,", username);
            long beginTime = System.currentTimeMillis();
            String result = accountMoneyService.drawAccountMoney(money, username, age);
            long endTime = System.currentTimeMillis();

            double seconds = (endTime - beginTime) / 1000.00;

            logger.info("{}{}", username, "接口耗时:[" + seconds + "]秒~");
            logger.info("DemoController request drawMoney result:{}, response succ", result);

            return result;
        } catch (Exception ex) {
            logger.warn("", ex);
        }

        return "fail";
    }


}
