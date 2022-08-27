package test.db.transaction.no_transaction;

import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/7/9
 * @description:
 */
public class TestNoTransactionDs1 extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs1Mapper();
        transactionService.runWithOutTransaction(false);
    }
}
