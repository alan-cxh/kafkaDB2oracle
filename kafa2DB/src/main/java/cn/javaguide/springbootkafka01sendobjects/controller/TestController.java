package cn.javaguide.springbootkafka01sendobjects.controller;

import cn.javaguide.springbootkafka01sendobjects.service.impl.KafkaToDbServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    @RequestMapping("test")
    public void test(@RequestParam int count) {
        String sql = "";
        for (int i = 0; i < count; i++) {
            jdbcTemplate.execute(sql);
        }
    }


    @RequestMapping("test1")
    public void test1() {
        logger.info("begin");
        for (int i = 0; i < 10000; i++) {
            String sql = "INSERT INTO PAAS2.AAA_SYS_USER (USER_ID, USERNAME, ACCOUNT, PASSWORD, DEPT, STATUS, PHONE, DESCRIPTION, CREATE_DATE, CHANGE_DATE, CREATOR_ID, DEL_FLAG, TEST1, TEST2) VALUES ( "+i + ", '运营用户', 'yyadmin', '$2a$12$UfEJYZUTNhETE8jIVV8gke1SMXvvSIC7R/b7CpXV0g1fKPPrOBId2', '12', 'enabled', '13712345680', '运营租户管理员', TO_DATE('2018-07-02 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2018-07-02 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 0, null, null)";
            jdbcTemplate.execute(sql);
        }
        logger.info("over");
    }

}
