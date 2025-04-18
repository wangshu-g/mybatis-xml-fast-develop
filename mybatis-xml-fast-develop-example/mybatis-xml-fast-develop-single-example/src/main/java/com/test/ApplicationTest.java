package com.test;

import com.wangshu.annotation.EnableConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@Slf4j
@EnableConfig(modelPackage = {"com.test.model"}, enableAutoInitTable = true)
@MapperScan(value = {"com.test.mapper"})
@SpringBootApplication
public class ApplicationTest implements CommandLineRunner {

    @Override
    public void run(String... args) {
//        log.info("args: {}", Arrays.asList(args));
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class);
    }

}
