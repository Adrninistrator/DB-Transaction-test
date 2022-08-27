package test.db.transaction.normal.commit.transaction_template;

import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalCommit_Ds1Mapper_Ds1TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs1Mapper();
        transactionService.useDs1TransactionTemplate();
        transactionService.runByTransactionTemplate(false);
    }
}
