package com.cp.personnel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.cp.common.security.annotation.EnableCustomConfig;

/**
 * @author tyt15
 */
@SpringBootApplication
@EnableCustomConfig
@MapperScan("com.cp.personnel.mapper")
public class CampusPassPersonnelApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusPassPersonnelApplication.class, args);
    }

}
