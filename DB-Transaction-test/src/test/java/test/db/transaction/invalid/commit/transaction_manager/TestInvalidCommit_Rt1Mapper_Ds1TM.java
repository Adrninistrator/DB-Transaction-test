package test.db.transaction.invalid.commit.transaction_manager;

import test.db.base.TestTransactionServiceBase;
import test.db.datasource.TestRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestInvalidCommit_Rt1Mapper_Ds1TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useRoutingMapper();
        TestRoutingDataSource.setDSKey1();
        transactionService.runByTransactionalDs1TM(false);
    }
}
