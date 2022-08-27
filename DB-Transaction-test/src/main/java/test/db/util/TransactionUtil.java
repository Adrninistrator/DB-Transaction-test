package test.db.util;

/**
 * @author adrninistrator
 * @date 2022/7/6
 * @description:
 */
public class TransactionUtil {
    // 记录开启事务时的连接ID（MySQL服务线程ID），在开启事务时记录，在提交事务/回滚事务/关闭连接时清除ThreadLocal
    private static final ThreadLocal<Long> threadLocalTxConnThreadId = new ThreadLocal<>();

    public static void setTxConnThreadId(long connThreadId) {
        threadLocalTxConnThreadId.set(connThreadId);
    }

    public static Long getTxConnThreadId() {
        return threadLocalTxConnThreadId.get();
    }

    public static void clearTxConnThreadId() {
        threadLocalTxConnThreadId.remove();
    }

    private TransactionUtil() {
        throw new IllegalStateException("illegal");
    }
}
