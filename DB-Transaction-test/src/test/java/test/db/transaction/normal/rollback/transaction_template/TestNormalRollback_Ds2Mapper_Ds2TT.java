package test.db.transaction.normal.rollback.transaction_template;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalRollback_Ds2Mapper_Ds2TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs2Mapper();
        transactionService.useDs2TransactionTemplate();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionTemplate(true));
    }
}
