package test.db.transaction.normal.commit.transaction_template;

import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestNormalCommit_Ds2Mapper_Ds2TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs2Mapper();
        transactionService.useDs2TransactionTemplate();
        transactionService.runByTransactionTemplate(false);
    }
}
