package test.db.transaction.normal.commit.transaction_manager;

import test.db.base.TestTransactionServiceBase;
import test.db.datasource.TestRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalCommit_Rt1Mapper_Rt1TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useRoutingMapper();
        TestRoutingDataSource.setDSKey1();
//        TestRoutingDataSource.clearThreadLocalDSKey();
        transactionService.runByTransactionalRtTM(false);
    }
}
