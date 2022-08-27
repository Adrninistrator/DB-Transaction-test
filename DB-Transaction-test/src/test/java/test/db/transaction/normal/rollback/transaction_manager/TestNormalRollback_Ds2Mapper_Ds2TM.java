package test.db.transaction.normal.rollback.transaction_manager;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalRollback_Ds2Mapper_Ds2TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs2Mapper();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionalDs2TM(true));
    }
}
