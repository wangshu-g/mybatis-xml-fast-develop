package com.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.test.model.*;
import com.test.service.*;
import com.wangshu.annotation.EnableConfig;
import com.wangshu.base.result.ResultBody;
import com.wangshu.tool.CommonParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@EnableConfig(modelPackage = {"com.test.model"}, enableAutoInitTable = true)
@MapperScan(value = {"com.test.mapper"})
@SpringBootApplication
public class ApplicationTest implements CommandLineRunner {

    @Override
    public void run(String... args) {
//        log.info("args: {}", Arrays.asList(args));
        test();
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTest.class);
    }

    @Value("${spring.profiles.active}")
    String active;
    @Resource
    UserService userService;
    @Resource
    ArticleGroupService articleGroupService;
    @Resource
    ArticleTagService articleTagService;
    @Resource
    ArticleService articleService;
    @Resource
    TypeTestModelService typeTestModelService;

    public void test() {
        userService.deleteAll();
        articleGroupService.deleteAll();
        articleTagService.deleteAll();
        articleService.deleteAll();
        typeTestModelService.deleteAll();
        for (int i = 0; i < 2; i++) {
            userService._save(new User().setId(String.valueOf(i)).setName("测试-" + UUID.randomUUID()));
            articleGroupService._save(new ArticleGroup().setId(String.valueOf(i)).setUid(String.valueOf(i)).setGroupName("测试-" + UUID.randomUUID()));
            for (int i1 = 0; i1 < 10; i1++) {
                articleService._save(new Article().setId(String.valueOf(i1)).setUid(String.valueOf(i)).setGroupId(String.valueOf(i)).setTitle("测试-" + UUID.randomUUID()).setDetail(UUID.randomUUID().toString()));
                articleTagService._save(new ArticleTag().setId(UUID.randomUUID().toString()).setArticleId(String.valueOf(i1)).setTagName("测试-" + UUID.randomUUID()));
                TypeTestModel temp = new TypeTestModel()
                        .setTest1("测试-test-1")
                        .setId(UUID.randomUUID().toString())
                        .setTest2(i)
                        .setTest3(Short.valueOf("1"))
                        .setTest4(Byte.valueOf("1"))
                        .setTest5('1')
                        .setTest6(i1 % 2 == 0)
//                                注意：这里除了 postgresql，目前其他支持的数据库，注意数值相关列要自行指定小数点后长度
                        .setTest7(BigDecimal.valueOf(1.379246248923358432662))
                        .setTest8(1.1f)
                        .setTest9(1.22d)
                        .setTest10(new Date(System.currentTimeMillis()))
                        .setTest11(new java.util.Date());
                typeTestModelService._save(temp);
                TypeTestModel first = new TypeTestModel()
                        .setTest1("test2").setId(UUID.randomUUID().toString());
                TypeTestModel second = new TypeTestModel()
                        .setTest1("test3").setId(UUID.randomUUID().toString());
                typeTestModelService._batchSave(Arrays.asList(first, second));
                log.info("{},{}", first.getIncrId(), second.getIncrId());
            }
        }
        String prefix = StrUtil.concat(false, CommonParam.getContextRunPath(), File.separator, "test-data", File.separator, active, File.separator);
        FileUtil.writeString(ResultBody.success(userService._getNestList()).toJsonyMdHms(), StrUtil.concat(false, prefix, "user.json"), StandardCharsets.UTF_8);
        FileUtil.writeString(ResultBody.success(articleGroupService._getNestList()).toJsonyMdHms(), StrUtil.concat(false, prefix, "articleGroup.json"), StandardCharsets.UTF_8);
        FileUtil.writeString(ResultBody.success(articleTagService._getNestList()).toJsonyMdHms(), StrUtil.concat(false, prefix, "articleTag.json"), StandardCharsets.UTF_8);
        FileUtil.writeString(ResultBody.success(articleService._getNestList("orderColumn", "title")).toJsonyMdHms(), StrUtil.concat(false, prefix, "article.json"), StandardCharsets.UTF_8);
        FileUtil.writeString(ResultBody.success(typeTestModelService._getNestList()).toJsonyMdHms(), StrUtil.concat(false, prefix, "typeTestModel.json"), StandardCharsets.UTF_8);
    }

}