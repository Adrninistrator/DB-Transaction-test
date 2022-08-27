package test.db.transaction.check;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import test.db.base.TestBase;
import test.db.util.PrintStackTraceSwitchUtil;

import javax.annotation.Resource;

/**
 * @author adrninistrator
 * @date 2022/8/20
 * @description:
 */
public class TestCheckTransactionStatus_SameTT extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TestCheckTransactionStatus_SameTT.class);

    @Resource(name = "ds1TransactionTemplate")
    private TransactionTemplate transactionTemplate;

    @Test
    public void test() {
        PrintStackTraceSwitchUtil.setSwitchOff();

        testOuter();
    }

    private void testOuter() {
        logger.info("testOuter 是否开启事务1: {}", TransactionSynchronizationManager.isActualTransactionActive() ? "是" : "否");

        transactionTemplate.execute(status -> {
            testInner();
            return null;
        });
    }

    private void testInner() {
        logger.info("testInner 是否开启事务1: {}", TransactionSynchronizationManager.isActualTransactionActive() ? "是" : "否");

        transactionTemplate.execute(status -> {
            logger.info("testInner 是否开启事务2: {}", TransactionSynchronizationManager.isActualTransactionActive() ? "是" : "否");
            return null;
        });
    }
}
