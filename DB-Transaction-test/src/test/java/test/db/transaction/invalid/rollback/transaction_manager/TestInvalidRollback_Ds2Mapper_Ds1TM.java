package test.db.transaction.invalid.rollback.transaction_manager;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestInvalidRollback_Ds2Mapper_Ds1TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs2Mapper();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionalDs1TM(true));
    }
}
