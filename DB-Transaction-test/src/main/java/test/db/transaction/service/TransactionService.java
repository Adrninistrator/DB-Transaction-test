package test.db.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import test.db.dao.TaskLockMapper;
import test.db.dao.ds1.Ds1TaskLockMapper;
import test.db.dao.ds2.Ds2TaskLockMapper;
import test.db.dao.routing.RoutingTaskLockMapper;
import test.db.entity.TaskLock;

import java.lang.management.ManagementFactory;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
@Service
public class TransactionService implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public static final String TEST_TASK_NAME = "TEST_DB_TRANSACTION";

    // 已锁定
    public static final int FLAG_LOCKED = 1;

    // 未锁定
    public static final int FLAG_UNLOCK = 0;

    private ApplicationContext applicationContext;

    private TransactionTemplate transactionTemplate;

    private TaskLockMapper taskLockMapper;

    protected String processInfo;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        processInfo = ManagementFactory.getRuntimeMXBean().getName();
        logger.info("当前进程信息 {}", processInfo);

        this.applicationContext = applicationContext;
    }

    public void useDs1Mapper() {
        taskLockMapper = applicationContext.getBean(Ds1TaskLockMapper.class);
    }

    public void useDs2Mapper() {
        taskLockMapper = applicationContext.getBean(Ds2TaskLockMapper.class);
    }

    public void useRoutingMapper() {
        taskLockMapper = applicationContext.getBean(RoutingTaskLockMapper.class);
    }

    public void useDs1TransactionTemplate() {
        transactionTemplate = applicationContext.getBean("ds1TransactionTemplate", TransactionTemplate.class);
    }

    public void useDs2TransactionTemplate() {
        transactionTemplate = applicationContext.getBean("ds2TransactionTemplate", TransactionTemplate.class);
    }

    public void useRoutingTransactionTemplate() {
        transactionTemplate = applicationContext.getBean("routingTransactionTemplate", TransactionTemplate.class);
    }

    public void runByTransactionTemplate(boolean throwException) {
        // 在事务中执行
        transactionTemplate.execute(status -> run(throwException));
    }

    @Transactional(transactionManager = "ds1TransactionManager", rollbackFor = Throwable.class)
    public void runByTransactionalDs1TM(boolean throwException) {
        run(throwException);
    }

    @Transactional(transactionManager = "ds2TransactionManager", rollbackFor = Throwable.class)
    public void runByTransactionalDs2TM(boolean throwException) {
        run(throwException);
    }

    @Transactional(transactionManager = "routingTransactionManager", rollbackFor = Throwable.class)
    public void runByTransactionalRtTM(boolean throwException) {
        run(throwException);
    }

    public void runWithOutTransaction(boolean throwException) {
        run(throwException);
    }

    public void runWrongTransaction(boolean throwException) {
        runByTransactionalDs1TM(throwException);
    }

    public Boolean run(boolean throwException) {
        Boolean success = doOperate(throwException);
        logger.info("执行结果 {}", success);
        return success;
    }

    protected Boolean doOperate(boolean throwException) {
        logger.info("是否开启事务 {}", TransactionSynchronizationManager.isActualTransactionActive() ? "是" : "否");

        // select for update
        TaskLock taskLock = taskLockMapper.selectForUpdate(TEST_TASK_NAME);
        logger.info("查询到的数据 {}", taskLock);

        // update
        TaskLock tmpTaskLock = new TaskLock();
        tmpTaskLock.setTaskName(TEST_TASK_NAME);
        tmpTaskLock.setLockFlag(FLAG_LOCKED);
        tmpTaskLock.setProcessInfo(processInfo);

        int row = taskLockMapper.updateLockFlagNoCheck(tmpTaskLock);
        logger.info("修改记录状态为锁定，更新行数 {} {}", row, TEST_TASK_NAME);

        if (throwException) {
            throw new RuntimeException("test");
        }

        return row > 0;
    }
}
