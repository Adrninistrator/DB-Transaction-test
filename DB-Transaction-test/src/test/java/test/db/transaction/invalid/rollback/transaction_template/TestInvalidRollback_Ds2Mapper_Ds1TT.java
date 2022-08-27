package test.db.transaction.invalid.rollback.transaction_template;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestInvalidRollback_Ds2Mapper_Ds1TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs2Mapper();
        transactionService.useDs1TransactionTemplate();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionTemplate(true));
    }
}
