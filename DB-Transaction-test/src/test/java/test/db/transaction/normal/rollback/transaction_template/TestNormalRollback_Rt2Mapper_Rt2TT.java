package test.db.transaction.normal.rollback.transaction_template;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;
import test.db.datasource.TestRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalRollback_Rt2Mapper_Rt2TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useRoutingMapper();
        transactionService.useRoutingTransactionTemplate();
        TestRoutingDataSource.setDSKey2();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionTemplate(true));
    }
}
