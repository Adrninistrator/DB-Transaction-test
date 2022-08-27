package test.db.transaction.invalid.rollback.transaction_template;

import org.junit.Assert;
import test.db.base.TestTransactionServiceBase;
import test.db.datasource.TestRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestInvalidRollback_Ds2Mapper_Rt2TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs2Mapper();
        transactionService.useRoutingTransactionTemplate();
        TestRoutingDataSource.setDSKey2();
        Assert.assertThrows(RuntimeException.class, () -> transactionService.runByTransactionTemplate(true));
    }
}
