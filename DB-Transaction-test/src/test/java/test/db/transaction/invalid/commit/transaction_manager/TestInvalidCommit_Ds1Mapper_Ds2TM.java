package test.db.transaction.invalid.commit.transaction_manager;

import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestInvalidCommit_Ds1Mapper_Ds2TM extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs1Mapper();
        transactionService.runByTransactionalDs2TM(false);
    }
}
