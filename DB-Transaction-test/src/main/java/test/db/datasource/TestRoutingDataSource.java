package test.db.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author adrninistrator
 * @date 2022/7/5
 * @description:
 */
public class TestRoutingDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> threadLocalDSKey = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return threadLocalDSKey.get();
    }

    public static void setDSKey1() {
        setDSKey("dataSourceKey1");
    }

    public static void setDSKey2() {
        setDSKey("dataSourceKey2");
    }

    private static void setDSKey(String dataSourceKey) {
        threadLocalDSKey.set(dataSourceKey);
    }

    public static void clearDSKey() {
        threadLocalDSKey.remove();
    }
}
