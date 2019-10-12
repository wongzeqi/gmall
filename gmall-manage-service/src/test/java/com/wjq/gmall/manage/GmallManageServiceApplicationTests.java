package com.wjq.gmall.manage;



import com.wjq.gmall.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageServiceApplicationTests {

    @Autowired
    RedisUtil redisUtil;


    @Test
    public void contextLoads() throws SQLException {
        Jedis jedis = redisUtil.getJedis();

        System.out.println(jedis);
    }

}
