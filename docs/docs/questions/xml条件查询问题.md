---
sidebar_position: 6
---

# xml 条件查询问题?

[参数名称生成对照](/docs/help/annotation/Column)

所有的条件查询生成模板为 select ... from ... where condition_1 and condition_2 and (or_condition_1 or or_condition_2) 

至于为什么使用此类模板，因为这样可以支持绝大多数业务场景