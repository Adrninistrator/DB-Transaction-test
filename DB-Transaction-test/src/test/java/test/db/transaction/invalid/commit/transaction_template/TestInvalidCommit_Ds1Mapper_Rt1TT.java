package test.db.transaction.invalid.commit.transaction_template;

import test.db.base.TestTransactionServiceBase;
import test.db.datasource.TestRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestInvalidCommit_Ds1Mapper_Rt1TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs1Mapper();
        transactionService.useRoutingTransactionTemplate();
        TestRoutingDataSource.setDSKey1();
        transactionService.runByTransactionTemplate(false);
    }
}
