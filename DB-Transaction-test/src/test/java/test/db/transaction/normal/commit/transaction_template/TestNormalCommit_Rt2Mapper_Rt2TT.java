package test.db.transaction.normal.commit.transaction_template;

import test.db.base.TestTransactionServiceBase;
import test.db.datasource.TestRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalCommit_Rt2Mapper_Rt2TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useRoutingMapper();
        transactionService.useRoutingTransactionTemplate();
        TestRoutingDataSource.setDSKey2();
        transactionService.runByTransactionTemplate(false);
    }
}
