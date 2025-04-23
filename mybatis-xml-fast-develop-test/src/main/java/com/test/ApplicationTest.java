package com.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.test.mapper.TypeTestModelMapper;
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

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
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
    @Resource
    TypeTestModelMapper typeTestModelMapper;

    public void test() {
        userService.deleteAll();
        articleGroupService.deleteAll();
        articleTagService.deleteAll();
        articleService.deleteAll();
        typeTestModelService.deleteAll();
        for (int i = 0; i < 2; i++) {
            userService._save(new User().setId(String.valueOf(i)).setName(UUID.randomUUID().toString()));
            articleGroupService._save(new ArticleGroup().setId(String.valueOf(i)).setUid(String.valueOf(i)).setGroupName(UUID.randomUUID().toString()));
            for (int i1 = 0; i1 < 10; i1++) {
                articleService._save(new Article().setId(String.valueOf(i1)).setUid(String.valueOf(i)).setGroupId(String.valueOf(i)).setTitle(UUID.randomUUID().toString()).setDetail(UUID.randomUUID().toString()));
                articleTagService._save(new ArticleTag().setId(UUID.randomUUID().toString()).setArticleId(String.valueOf(i1)).setTagName(UUID.randomUUID().toString()));
                typeTestModelMapper.insert(
                        new TypeTestModel()
//                                注意：oracle id 设置为自增列插入出现异常，详情见：oracle-test-error.log
//                                .setId()
                                .setTest1("test1")
                                .setTest2(i)
                                .setTest3(Short.valueOf("1"))
                                .setTest4(Byte.valueOf("1"))
                                .setTest5('1')
                                .setTest6(true)
//                                注意：除了 postgresql，目前其他支持的数据库，注意数值相关要自行指定小数点后长度
                                .setTest7(BigDecimal.valueOf(1.01))
                                .setTest8(1.1f)
                                .setTest9(1.22d)
                                .setTest10(new Date(System.currentTimeMillis()))
                                .setTest11(new java.util.Date())
                );
                typeTestModelService._save(
                        new TypeTestModel()
//                                注意：oracle id 设置为自增列插入出现异常，详情见：oracle-test-error.log
//                                .setId()
                                .setTest1("test1")
                                .setTest2(i)
                                .setTest3(Short.valueOf("1"))
                                .setTest4(Byte.valueOf("1"))
                                .setTest5('1')
                                .setTest6(true)
//                                注意：除了 postgresql，目前其他支持的数据库，注意数值相关要自行指定小数点后长度
                                .setTest7(BigDecimal.valueOf(1.01))
                                .setTest8(1.1f)
                                .setTest9(1.22d)
                                .setTest10(new Date(System.currentTimeMillis()))
                                .setTest11(new java.util.Date())
                );
            }
        }
        String prefix = StrUtil.concat(false, CommonParam.getContextRunPath(), "/", "test-data/", active, "/");
        FileUtil.writeString(ResultBody.success(userService._getNestList()).toJsonyMdHms(), StrUtil.concat(false, prefix, "user.json"), StandardCharsets.UTF_8);
        FileUtil.writeString(ResultBody.success(articleGroupService._getNestList()).toJsonyMdHms(), StrUtil.concat(false, prefix, "articleGroup.json"), StandardCharsets.UTF_8);
        FileUtil.writeString(ResultBody.success(articleTagService._getNestList()).toJsonyMdHms(), StrUtil.concat(false, prefix, "articleTag.json"), StandardCharsets.UTF_8);
        FileUtil.writeString(ResultBody.success(articleService._getNestList("orderColumn", "title")).toJsonyMdHms(), StrUtil.concat(false, prefix, "article.json"), StandardCharsets.UTF_8);
        FileUtil.writeString(ResultBody.success(typeTestModelService._getNestList()).toJsonyMdHms(), StrUtil.concat(false, prefix, "typeTestModel.json"), StandardCharsets.UTF_8);
    }

}
