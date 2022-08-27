package test.db.transaction.no_transaction;

import test.db.base.TestTransactionServiceBase;

/**
 * @author adrninistrator
 * @date 2022/7/13
 * @description:
 */
public class TestWrongTransactionDs1 extends TestTransactionServiceBase {

    @Override
    protected void doTest() {
        transactionService.useDs1Mapper();
        transactionService.runWrongTransaction(false);
    }
}
