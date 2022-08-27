package test.db.transaction.invalid.commit.transaction_template;

import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/6/21
 * @description:
 */
public class TestInvalidCommit_Ds1Mapper_Ds2TT extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs1Mapper();
        transactionService.useDs2TransactionTemplate();
        transactionService.runByTransactionTemplate(false);
    }
}
