package test.db.transaction.normal.commit.transaction_manager;

import test.db.base.TestTransactionServiceBase;
import test.db.datasource.TestRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalCommit_Rt2Mapper_Rt2TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useRoutingMapper();
        TestRoutingDataSource.setDSKey2();
        transactionService.runByTransactionalRtTM(false);
    }
}
