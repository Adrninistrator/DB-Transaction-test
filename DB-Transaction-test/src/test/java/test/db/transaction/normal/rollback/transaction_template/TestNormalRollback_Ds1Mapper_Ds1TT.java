package test.db.transaction.normal.rollback.transaction_template;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalRollback_Ds1Mapper_Ds1TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs1Mapper();
        transactionService.useDs1TransactionTemplate();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionTemplate(true));
    }
}
