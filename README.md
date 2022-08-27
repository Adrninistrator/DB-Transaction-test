# 1. 前言

当前项目用于对Spring、MyBatis、Druid、MySQL执行SQL语句与事务的分析及监控进行验证。

# 2. !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
以下为验证代码的使用说明，相关文档可参考[]()。

# 3. 配置MySQL数据库信息

在DB-Transaction-test/src/main/resources/application.properties配置文件中配置MySQL数据库信息。

# 4. 执行验证代码

验证代码在DB-Transaction-test/src/test/java/test/db/transaction目录中，每个包中的测试代码代表了一种验证的场景，如下所示：

|包名|说明|
|---|---|
|check|检查当前方法是否在事务中执行|
|invalid.commit.transaction_manager|事务失效，执行提交操作，通过@Transactional注解使用事务|
|invalid.commit.transaction_template|事务失效，执行提交操作，通过事务模板使用事务|
|invalid.rollback.transaction_manager|事务失效，执行回滚操作，通过@Transactional注解使用事务|
|invalid.rollback.transaction_template|事务失效，执行回滚操作，通过事务模板使用事务|
|no_transaction|不使用事务执行SQL语句|
|normal.commit.transaction_manager|事务生效，执行提交操作，通过@Transactional注解使用事务|
|normal.commit.transaction_template|事务生效，执行提交操作，通过事务模板使用事务|
|normal.rollback.transaction_manager|事务生效，执行回滚操作，通过@Transactional注解使用事务|
|normal.rollback.transaction_template|事务生效，执行回滚操作，通过事务模板使用事务|

# 5. 日志查看

以上代码执行完毕后，相关的执行日志保存在log-print_stack_trace_xxx目录中，log-print_stack_trace_off目录中是未打印调用堆栈的日志，log-print_stack_trace_on目录中是打印调用堆栈的日志。
