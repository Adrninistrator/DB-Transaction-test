package test.db.base;

import org.apache.logging.log4j.core.config.Configurator;
import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import test.db.common.CommonConstants;
import test.db.datasource.TestRoutingDataSource;
import test.db.transaction.service.TransactionService;
import test.db.util.DbUtil;
import test.db.util.PrintStackTraceSwitchUtil;

/**
 * @author adrninistrator
 * @date 2022/7/5
 * @description:
 */
// 指定测试方法以字典顺序执行，使打印调用堆栈的方法后执行，在日志中能够出现关闭连接的日志
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
public abstract class TestTransactionServiceBase extends TestBase {

    @Autowired
    protected TransactionService transactionService;

    public TestTransactionServiceBase() {
        System.setProperty("druid.mysql.usePingMethod", "false");

        System.setProperty(CommonConstants.TEST_MODE_KEY, this.getClass().getSimpleName());
    }

    @After
    public void afterTestTransactionServiceBase() {
        TestRoutingDataSource.clearDSKey();
    }

    private void commonOperate() {
        // 刷新log4j配置
        Configurator.reconfigure();

        Assert.assertNotNull(applicationContext);
        // 重启数据源
        DbUtil.restartDataSources(applicationContext);

        doTest();
    }

    @Test
    public void testPrintStackTrace1Off() {
        PrintStackTraceSwitchUtil.setSwitchOff();

        commonOperate();
    }

    @Test
    public void testPrintStackTrace2On() {
        PrintStackTraceSwitchUtil.setSwitchOn();

        commonOperate();
    }

    protected abstract void doTest();
}
