---
sidebar_position: 2
---

# xml

xml 会根据模块名称自动生成到 resources/[模块名称-mapper] 文件夹下

大致结构和生成的默认方法

```xml
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.test.mapper.ArticleMapper/>
 
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
 
  <insert id="_save" parameterType="Map"/>
 
  <insert id="_batchSave" parameterType="List"/>
 
  <delete id="_delete" parameterType="Map"/>
 
  <update id="_update" parameterType="Map"/>
 
  <select id="_select" parameterType="Map" resultType="com.test.model.Article" resultMap="Article"/>
 
  <select id="_getList" parameterType="Map" resultType="Map"/>
 
  <select id="_getNestList" parameterType="Map" resultType="com.test.model.Article" resultMap="Article"/>
 
  <select id="_getTotal" parameterType="Map" resultType="Integer"/>
  
</mapper>

```