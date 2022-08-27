package test.db.transaction.invalid.rollback.transaction_manager;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;
import test.db.datasource.TestRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestInvalidRollback_Rt2Mapper_Ds2TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useRoutingMapper();
        TestRoutingDataSource.setDSKey2();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionalDs2TM(true));
    }
}
