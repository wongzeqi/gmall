package com.wjq.gmall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


//java.lang.NoSuchMethodException: tk.mybatis.mapper.provider.base.BaseSelectProvider.<init>()
//在使用通用mapper的时候 必须要使用通用mapper的扫描器   并且扫描的位置

/*
通用mapper的整合
1  导入pom
<!-- 通用mapper -->
        <!--java.util.NoSuchElementException: No value bound  同意mapper版本过低会报错 必须和springboot版本契合-->
        <dependency>
            <groupId>tk.mybatis</groupId>
            <artifactId>mapper-spring-boot-starter</artifactId>
            <version>2.0.2</version>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-jdbc</artifactId>
                </exclusion>
            </exclusions>
        </dependency>


  2  继承 将自己的Mapper接口  继承通用 Mapper接口

  3. 配置 通用mapper 的主键返回策略

  4.扫描器
    使用通用mapper的扫描器
 */
@SpringBootApplication
@MapperScan(basePackages = "com.wjq.gmall.user.mapper")
public class GmallUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallUserApplication.class, args);
    }

}
