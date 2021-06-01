package com.ming.ls;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackages = {
        "com.ming.ls.modules.sys.controller",
        "com.ming.ls.modules.sys.service",
        "com.ming.ls.config",
        "com.ming.ls.modules.sys.job",
        "com.ming.ls.modules.common.controller",
        "com.ming.ls.modules.common.intercept",
        "com.ming.ls.modules.common.exception",
        "com.ming.ls.modules.common.mq",
})
@EnableTransactionManagement   //开启事务管理
// 与dao层的@Mapper二选一写上即可(主要作用是扫包)
@MapperScan("com.ming.ls.modules.sys.dao")
@SpringBootApplication()
public class LaundrySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaundrySystemApplication.class, args);
    }

}
