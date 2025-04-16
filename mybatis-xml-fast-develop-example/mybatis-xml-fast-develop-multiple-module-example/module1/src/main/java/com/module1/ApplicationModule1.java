package com.module1;

import com.wangshu.annotation.EnableConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Arrays;


@Slf4j
@EnableConfig(modelPackage = {"com.module1.model"})
@MapperScan(value = {"com.module1.mapper"})
@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
public class ApplicationModule1 implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("args: {}", Arrays.asList(args));
    }

    public static void main(String[] args) {

    }

}
