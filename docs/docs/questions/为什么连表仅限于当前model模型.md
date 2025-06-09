---
sidebar_position: 4
---

# 为什么连表仅限于当前 model 模型？

可控的关联深度，避免复杂关系映射可能带来的间接查询扩散

生成的 xml 连表，不会去分析关联实体内部的关联关系，限制在当前实体内

```xml
<resultMap id="Article" type="com.test.model.Article">
    <id column="id" jdbcType="VARCHAR" property="id"/>
    <result column="groupId" jdbcType="VARCHAR" property="groupId"/>
    <result column="uid" jdbcType="VARCHAR" property="uid"/>
    <result column="status" jdbcType="VARCHAR" property="status"/>
    <result column="detail" jdbcType="VARCHAR" property="detail"/>
    <result column="desc" jdbcType="VARCHAR" property="desc"/>
    <result column="title" jdbcType="VARCHAR" property="title"/>
    <result column="deletedAt" jdbcType="TIMESTAMP" property="deletedAt"/>
    <result column="updatedAt" jdbcType="TIMESTAMP" property="updatedAt"/>
    <result column="createdAt" jdbcType="TIMESTAMP" property="createdAt"/>
    <association property="user" javaType="com.test.model.User">
      <id column="userModelId" jdbcType="VARCHAR" property="id"/>
      <result column="userModelReadMe" jdbcType="VARCHAR" property="readMe"/>
      <result column="userModelName" jdbcType="VARCHAR" property="name"/>
      <result column="userModelDeletedAt" jdbcType="TIMESTAMP" property="deletedAt"/>
      <result column="userModelUpdatedAt" jdbcType="TIMESTAMP" property="updatedAt"/>
      <result column="userModelCreatedAt" jdbcType="TIMESTAMP" property="createdAt"/>
    </association>
    <association property="articleGroup" javaType="com.test.model.ArticleGroup">
      <id column="articleGroupModelId" jdbcType="VARCHAR" property="id"/>
      <result column="articleGroupModelUid" jdbcType="VARCHAR" property="uid"/>
      <result column="articleGroupModelDesc" jdbcType="VARCHAR" property="desc"/>
      <result column="articleGroupModelGroupName" jdbcType="VARCHAR" property="groupName"/>
      <result column="articleGroupModelDeletedAt" jdbcType="TIMESTAMP" property="deletedAt"/>
      <result column="articleGroupModelUpdatedAt" jdbcType="TIMESTAMP" property="updatedAt"/>
      <result column="articleGroupModelCreatedAt" jdbcType="TIMESTAMP" property="createdAt"/>
    </association>
    <collection property="articleTagList" ofType="com.test.model.ArticleTag" column="{articleId=id}" select="com.test.mapper.ArticleTagMapper._getNestList"/>
  </resultMap>
```