package test.db.transaction.normal.rollback.transaction_manager;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalRollback_Ds1Mapper_Ds1TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs1Mapper();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionalDs1TM(true));
    }
}
