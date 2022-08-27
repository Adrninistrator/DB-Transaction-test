package test.db.transaction.normal.rollback.transaction_template;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;
import test.db.datasource.TestRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalRollback_Rt1Mapper_Rt1TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useRoutingMapper();
        transactionService.useRoutingTransactionTemplate();
//        TestRoutingDataSource.setThreadLocalDSKey1();
        TestRoutingDataSource.clearDSKey();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionTemplate(true));
    }
}
