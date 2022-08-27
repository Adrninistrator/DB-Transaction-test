package test.db.druid_filter.filter;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.mysql.cj.NativeSession;
import com.mysql.cj.jdbc.ConnectionImpl;
import com.mysql.cj.protocol.a.NativeProtocol;
import com.mysql.cj.protocol.a.NativeServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import test.db.druid_filter.dto.SqlSessionInfo;
import test.db.util.CommonUtil;
import test.db.util.DbUtil;
import test.db.util.TransactionUtil;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author adrninistrator
 * @date 2022/6/22
 * @description:
 */
public class DruidMonitorFilter extends FilterEventAdapter {
    private static final Logger logger = LoggerFactory.getLogger("druidMonitorFilter");

    /*
        连接ID（MySQL服务线程ID）与对应的数据源名称Map
        在statementExecuteBefore方法中，为了能够获取其他数据源的名称，需要将当前变量设置为静态变量
     */
    private static final Map<Long, String> connThreadIdAndDataSourceNameMap = new ConcurrentHashMap<>();

    // 记录当前连接的SQL语句执行次数，在执行SQL语句时记录执行次数，关闭连接时清除Map中的对应数据
    private final Map<Long, AtomicInteger> execSqlTimesMap = new ConcurrentHashMap<>();

    public void initFilter() {
        logger.debug("创建实例 {}", this);
    }

    public void destroyFilter() {
        connThreadIdAndDataSourceNameMap.clear();
        execSqlTimesMap.clear();
    }

    @Override
    public DruidPooledConnection dataSource_getConnection(FilterChain chain, DruidDataSource dataSource, long maxWaitMillis) throws SQLException {
        DruidPooledConnection druidPooledConnection = super.dataSource_getConnection(chain, dataSource, maxWaitMillis);

        if (logger.isDebugEnabled()) {
            try {
                ConnectionImpl connectionImpl = getConnectionImplFromDruidPooledConnection(druidPooledConnection);
                if (connectionImpl != null) {
                    String dataSourceName = dataSource.getName();

                    SqlSessionInfo sqlSessionInfo = getSqlSessionInfoFromConnectionImpl(connectionImpl);
                    sqlSessionInfo.setDataSourceName(dataSourceName);

                    logger.debug("### [从连接池借出连接] connThreadId {} dataSourceName {}\n{}", sqlSessionInfo.getConnThreadId(), dataSourceName, sqlSessionInfo);
                    // 记录连接ID（MySQL服务线程ID）与对应的数据源名称
                    connThreadIdAndDataSourceNameMap.put(sqlSessionInfo.getConnThreadId(), dataSourceName);
                }

                CommonUtil.printStackTrace();
            } catch (Exception e) {
                logger.error("error ", e);
            }
        }

        return druidPooledConnection;
    }

    @Override
    public void connection_connectAfter(ConnectionProxy connection) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        try {
            SqlSessionInfo sqlSessionInfo = getSqlSessionInfoFromConnectionProxy(connection);
            logger.debug("### [创建连接] connThreadId {} {}\n{}", sqlSessionInfo.getConnThreadId(), getAutoCommitFlag(sqlSessionInfo), sqlSessionInfo);

            // 查询processlist表
            selectProcessList(connection.getRawObject());

            CommonUtil.printStackTrace();
        } catch (Exception e) {
            logger.error("error ", e);
        }
    }

    @Override
    public void dataSource_releaseConnection(FilterChain chain, DruidPooledConnection connection) throws SQLException {
        if (logger.isDebugEnabled()) {
            try {
                SqlSessionInfo sqlSessionInfo = getSqlSessionInfoFromDruidPooledConnection(connection);
                logger.debug("### [归还连接至连接池] connThreadId {} {} 当前连接SQL语句执行次数: {}\n{}", sqlSessionInfo.getConnThreadId(), getAutoCommitFlag(sqlSessionInfo),
                        getExecSqlTimes(sqlSessionInfo.getConnThreadId()), sqlSessionInfo);

                clearExecSqlTimesWithConnThreadId(sqlSessionInfo.getConnThreadId());

                CommonUtil.printStackTrace();
            } catch (Exception e) {
                logger.error("error ", e);
            }
        }

        super.dataSource_releaseConnection(chain, connection);
    }

    @Override
    public void connection_close(FilterChain chain, ConnectionProxy connection) throws SQLException {
        if (logger.isDebugEnabled()) {
            try {
                SqlSessionInfo sqlSessionInfo = getSqlSessionInfoFromConnectionProxy(connection);
                logger.debug("### [关闭连接] connThreadId {} {}\n{}", sqlSessionInfo.getConnThreadId(), getAutoCommitFlag(sqlSessionInfo), sqlSessionInfo);

                connThreadIdAndDataSourceNameMap.remove(sqlSessionInfo.getConnThreadId());

                TransactionUtil.clearTxConnThreadId();

                CommonUtil.printStackTrace();
            } catch (Exception e) {
                logger.error("error ", e);
            }
        }

        super.connection_close(chain, connection);
    }

    /**
     * statementExecuteQueryBefore、statementExecuteUpdateBefore方法相关的操作都会调用到statementExecuteBefore
     *
     * @param statement
     * @param sql
     */
    @Override
    protected void statementExecuteBefore(StatementProxy statement, String sql) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        try {
            SqlSessionInfo sqlSessionInfo = getSqlSessionInfoFromConnectionProxy(statement.getConnectionProxy());
            String sqlFormatted = sql.replaceAll("\n[ \t]*\n", "\n");
            logger.debug("### [执行SQL语句] connThreadId {} {}\n{}\n{}", sqlSessionInfo.getConnThreadId(), getAutoCommitFlag(sqlSessionInfo), sqlFormatted, sqlSessionInfo);
            logger.debug("当前{}开启事务", TransactionSynchronizationManager.isActualTransactionActive() ? "已" : "未");

            // 检查执行SQL语句的连接ID（MySQL服务线程ID）与开启事务时的是否相同
            Long connThreadIdInThreadLocal = TransactionUtil.getTxConnThreadId();
            Long actualConnThreadId = sqlSessionInfo.getConnThreadId();
            if (connThreadIdInThreadLocal != null) {
                if (!connThreadIdInThreadLocal.equals(actualConnThreadId)) {
                    logger.debug("@@@ 执行SQL语句的连接ID（MySQL服务线程ID）与开启事务时的不同 {}@[{}] {}@[{}]", actualConnThreadId, getDsName(actualConnThreadId),
                            connThreadIdInThreadLocal, getDsName(connThreadIdInThreadLocal));
                } else {
                    logger.debug("@@@ 执行SQL语句的连接ID（MySQL服务线程ID）与开启事务时的相同 {}@[{}]", actualConnThreadId, getDsName(actualConnThreadId));
                }
            } else {
                logger.debug("@@@ ThreadLocal为空");
            }
            // 在Map中记录SQL语句执行次数
            addExecSqlTimes(actualConnThreadId);

            CommonUtil.printStackTrace();
        } catch (Exception e) {
            logger.error("error ", e);
        }
    }

    @Override
    public void connection_commit(FilterChain chain, ConnectionProxy connection) throws SQLException {
        if (logger.isDebugEnabled()) {
            try {
                SqlSessionInfo sqlSessionInfo = getSqlSessionInfoFromConnectionProxy(connection);
                logger.debug("### [提交事务] connThreadId {} {} 当前连接SQL语句执行次数: {}\n{}", sqlSessionInfo.getConnThreadId(), getAutoCommitFlag(sqlSessionInfo),
                        getExecSqlTimes(sqlSessionInfo.getConnThreadId()), sqlSessionInfo);
            } catch (Exception e) {
                logger.error("error ", e);
            }
            TransactionUtil.clearTxConnThreadId();

            CommonUtil.printStackTrace();
        }

        super.connection_commit(chain, connection);
    }

    @Override
    public void connection_rollback(FilterChain chain, ConnectionProxy connection) throws SQLException {
        if (logger.isDebugEnabled()) {
            try {
                SqlSessionInfo sqlSessionInfo = getSqlSessionInfoFromConnectionProxy(connection);
                logger.debug("### [回滚事务] connThreadId {} {}\n{}", sqlSessionInfo.getConnThreadId(), getAutoCommitFlag(sqlSessionInfo), sqlSessionInfo);

                TransactionUtil.clearTxConnThreadId();

                CommonUtil.printStackTrace();
            } catch (Exception e) {
                logger.error("error ", e);
            }
        }

        super.connection_rollback(chain, connection);
    }

    @Override
    public void connection_rollback(FilterChain chain, ConnectionProxy connection, Savepoint savepoint) throws SQLException {
        if (logger.isDebugEnabled()) {
            try {
                SqlSessionInfo sqlSessionInfo = getSqlSessionInfoFromConnectionProxy(connection);
                logger.debug("### [回滚事务savepoint] connThreadId {} {}\n{}", sqlSessionInfo.getConnThreadId(), getAutoCommitFlag(sqlSessionInfo), sqlSessionInfo);

                TransactionUtil.clearTxConnThreadId();

                CommonUtil.printStackTrace();
            } catch (Exception e) {
                logger.error("error ", e);
            }
        }

        super.connection_rollback(chain, connection, savepoint);
    }

    @Override
    public void connection_setAutoCommit(FilterChain chain, ConnectionProxy connection, boolean autoCommit) throws SQLException {
        if (logger.isDebugEnabled()) {
            try {
                SqlSessionInfo sqlSessionInfo = getSqlSessionInfoFromConnectionProxy(connection);
                logger.debug("### [{}自动提交] connThreadId {} {}\n{}", autoCommit ? "开启" : "关闭", sqlSessionInfo.getConnThreadId(), getAutoCommitFlag(sqlSessionInfo), sqlSessionInfo);

                if (autoCommit) {
                    // 开启自动提交，当作提交事务
                    TransactionUtil.clearTxConnThreadId();
                } else {
                    // 关闭自动提交，当作开始事务
                    // 在ThreadLocal中记录连接ID（MySQL服务线程ID）
                    TransactionUtil.setTxConnThreadId(sqlSessionInfo.getConnThreadId());
                }

                CommonUtil.printStackTrace();
            } catch (Exception e) {
                logger.error("error ", e);
            }
        }

        super.connection_setAutoCommit(chain, connection, autoCommit);
    }

    private String getDsName(Long connThreadId) {
        return connThreadIdAndDataSourceNameMap.get(connThreadId);
    }

    // 查询processlist表
    private void selectProcessList(Connection connection) {
        Map<String, Object> processListMap = DbUtil.queryOneRow(connection, "select * from information_schema.processlist where id = CONNECTION_ID()", null);
        if (processListMap == null) {
            return;
        }
        logger.debug("\nprocesslist ID [{}] USER [{}] HOST [{}] DB [{}] COMMAND [{}] INFO [{}]",
                processListMap.get("ID"),
                processListMap.get("USER"),
                processListMap.get("HOST"),
                processListMap.get("DB"),
                processListMap.get("COMMAND"),
                processListMap.get("INFO"));
    }

    private ConnectionImpl checkCastConnectionImpl(Connection connection) {
        if (!(connection instanceof ConnectionImpl)) {
            logger.error("connection不是ConnectionImpl实例 {}", connection.getClass().getName());
            return null;
        }

        return (ConnectionImpl) connection;
    }

    private ConnectionImpl getConnectionImplFromDruidPooledConnection(DruidPooledConnection druidPooledConnection) {
        DruidConnectionHolder druidConnectionHolder = druidPooledConnection.getConnectionHolder();
        if (druidConnectionHolder == null) {
            logger.error("druidPooledConnection.getConnectionHolder()为空");
            return null;
        }

        Connection connection = druidConnectionHolder.getConnection();
        if (!(connection instanceof ConnectionProxyImpl)) {
            logger.error("connection不是ConnectionImpl实例 {}", connection.getClass().getName());
            return null;
        }

        ConnectionProxyImpl connectionProxyImpl = (ConnectionProxyImpl) connection;
        return checkCastConnectionImpl(connectionProxyImpl.getRawObject());
    }

    private SqlSessionInfo getSqlSessionInfoFromConnectionProxy(ConnectionProxy connection) throws IOException, SQLException {
        /*
            以下druidTrxId获取的是Druid内部的事务ID，与MySQL服务中的事务ID无关
         */
//        long druidTrxId = -1;
//        if (connection.getTransactionInfo() != null) {
//            druidTrxId = connection.getTransactionInfo().getId();
//        }

        return getSqlSessionInfoFromConnection(connection.getRawObject());
    }

    private SqlSessionInfo getSqlSessionInfoFromDruidPooledConnection(DruidPooledConnection druidPooledConnection) throws SQLException, IOException {
        ConnectionImpl connectionImpl = getConnectionImplFromDruidPooledConnection(druidPooledConnection);
        return getSqlSessionInfoFromConnectionImpl(connectionImpl);
    }

    private SqlSessionInfo getSqlSessionInfoFromConnection(Connection connection) throws IOException, SQLException {
        ConnectionImpl connectionImpl = checkCastConnectionImpl(connection);
        return getSqlSessionInfoFromConnectionImpl(connectionImpl);
    }

    private SqlSessionInfo getSqlSessionInfoFromConnectionImpl(ConnectionImpl connectionImpl) throws IOException, SQLException {
        SqlSessionInfo sqlSessionInfo = new SqlSessionInfo();
        if (connectionImpl == null) {
            return sqlSessionInfo;
        }

        sqlSessionInfo.setConnectionHashCode(CommonUtil.getObjHashCodeStr(connectionImpl));

        NativeSession nativeSession = connectionImpl.getSession();
        // connectionImpl.getId()为连接ID，与MySQL服务线程ID相同
        sqlSessionInfo.setConnThreadId(nativeSession.getThreadId());
        // 根据连接ID获取对应的数据源名称
        sqlSessionInfo.setDataSourceName(getDsName(nativeSession.getThreadId()));

        NativeProtocol nativeProtocol = nativeSession.getProtocol();
        Socket socket = nativeProtocol.getSocketConnection().getMysqlSocket();
        sqlSessionInfo.setLocalPort(socket.getLocalPort());

        NativeServerSession nativeServerSession = nativeProtocol.getServerSession();
        // NativeServerSession.inTransactionOnServer()可能不是最新状态，不使用
        // 服务器是否自动提交，需要使用大写的isAutoCommit()方法，不使用小写的isAutocommit()方法
        sqlSessionInfo.setAutoCommit(nativeServerSession.isAutoCommit());
        sqlSessionInfo.setServerVersion(nativeServerSession.getServerVersion().toString());
        sqlSessionInfo.setServerIP(socket.getInetAddress().getHostAddress());
        sqlSessionInfo.setServerPort(socket.getPort());
        sqlSessionInfo.setDbName(connectionImpl.getDatabase());
        sqlSessionInfo.setUserName(connectionImpl.getUser());

        return sqlSessionInfo;
    }

    private String getAutoCommitFlag(SqlSessionInfo sqlSessionInfo) {
        return sqlSessionInfo.isAutoCommit() ? "自动提交" : "不自动提交";
    }

    private void addExecSqlTimes(Long connThreadId) {
        AtomicInteger atomicInteger = execSqlTimesMap.computeIfAbsent(connThreadId, k -> new AtomicInteger(0));
        atomicInteger.addAndGet(1);
    }

    private int getExecSqlTimes(Long connThreadId) {
        AtomicInteger atomicInteger = execSqlTimesMap.get(connThreadId);
        if (atomicInteger == null) {
            return 0;
        }

        return atomicInteger.get();
    }

    private void clearExecSqlTimesWithConnThreadId(Long connThreadId) {
        execSqlTimesMap.remove(connThreadId);
    }
}
