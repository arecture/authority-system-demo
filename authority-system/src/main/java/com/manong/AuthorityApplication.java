package com.manong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName AuthorityApplication
 * @Description TODO
 * @Author 40706
 * @Date 2022/7/1 15:01
 * @Version
 **/
@MapperScan("com.manong.dao")
@SpringBootApplication
public class AuthorityApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorityApplication.class,args);
    }
}
