# 1. 前言

当前项目用于Spring、MyBatis、Druid、MySQL执行SQL语句与事务分析及监控。

以下示例项目的使用说明，相关文档可参考以下内容：

“Spring、MyBatis、Druid、MySQL不使用事务执行SQL语句分析”[https://blog.csdn.net/a82514921/article/details/126563515](https://blog.csdn.net/a82514921/article/details/126563515)

“Spring、MyBatis、Druid、MySQL使用事务执行SQL语句分析”[https://blog.csdn.net/a82514921/article/details/126563542](https://blog.csdn.net/a82514921/article/details/126563542)

“Spring、MyBatis、Druid、MySQL执行SQL语句与事务监控”[https://blog.csdn.net/a82514921/article/details/126563558](https://blog.csdn.net/a82514921/article/details/126563558)

“数据源使用错误导致MySQL事务失效分析”[https://blog.csdn.net/a82514921/article/details/126563573](https://blog.csdn.net/a82514921/article/details/126563573)

# 3. 配置MySQL数据库信息

在DB-Transaction-test/src/main/resources/application.properties配置文件中配置MySQL数据库信息。

# 4. 执行示例项目

示例项目在DB-Transaction-test/src/test/java/test/db/transaction目录中

|包名|说明|
|---|---|
|check||
|invalid.commit.transaction_manager||
|invalid.commit.transaction_template||
|invalid.rollback.transaction_manager||
|invalid.rollback.transaction_template||
|no_transaction||
|normal.commit.transaction_manager||
|normal.commit.transaction_template||
|normal.rollback.transaction_manager||
|normal.rollback.transaction_template||

# 日志查看

以上代码执行完毕后，相关的执行日志保存在log-print_stack_trace_xxx目录中，log-print_stack_trace_off目录中是未打印调用堆栈的日志，log-print_stack_trace_on目录中是打印调用堆栈的日志。
