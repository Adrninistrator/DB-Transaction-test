package test.db.transaction.normal.commit.transaction_manager;

import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalCommit_Ds1Mapper_Ds1TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs1Mapper();
        transactionService.runByTransactionalDs1TM(false);
    }
}
