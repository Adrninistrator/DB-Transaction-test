package test.db.transaction.normal.commit.transaction_manager;

import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalCommit_Ds2Mapper_Ds2TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs2Mapper();
        transactionService.runByTransactionalDs2TM(false);
    }
}
