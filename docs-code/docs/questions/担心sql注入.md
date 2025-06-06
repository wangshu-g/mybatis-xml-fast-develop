---
sidebar_position: 3
---

# 担心 sql 注入？

所有生成的 xml sql 语句中，只有 `${orderColumn}` 和 `${order}` 采用了拼接字符串的方式

而这两个参数可以预测所有有效值，详情见 AbstractBaseDataService