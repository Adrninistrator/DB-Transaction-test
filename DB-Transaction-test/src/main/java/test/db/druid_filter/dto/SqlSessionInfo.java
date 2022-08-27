package test.db.druid_filter.dto;

/**
 * @author adrninistrator
 * @date 2022/6/22
 * @description:
 */
public class SqlSessionInfo {

    // com.mysql.cj.jdbc.ConnectionImpl对象HashCode
    private String connectionHashCode;

    // 连接ID（MySQL服务线程ID）
    private long connThreadId;

    // 连接的本地端口
    private int localPort;

    // 数据源名称
    private String dataSourceName;

    // 自动提交开关
    private boolean autoCommit;

    // MySQL服务IP
    private String serverIP;

    // MySQL服务端口
    private int serverPort;

    // MySQL数据库名称
    private String dbName;

    // MySQL用户名
    private String userName;

    // MySQL版本
    private String serverVersion;

    public String getConnectionHashCode() {
        return connectionHashCode;
    }

    public void setConnectionHashCode(String connectionHashCode) {
        this.connectionHashCode = connectionHashCode;
    }

    public long getConnThreadId() {
        return connThreadId;
    }

    public void setConnThreadId(long connThreadId) {
        this.connThreadId = connThreadId;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    @Override
    public String toString() {
        return "SqlSessionInfo{" +
                "connectionHashCode='" + connectionHashCode + '\'' +
                ", connThreadId=" + connThreadId +
                ", localPort=" + localPort +
                ", dataSourceName='" + dataSourceName + '\'' +
                ", autoCommit=" + autoCommit +
                ", serverIP='" + serverIP + '\'' +
                ", serverPort=" + serverPort +
                ", dbName='" + dbName + '\'' +
                ", userName='" + userName + '\'' +
                ", serverVersion='" + serverVersion + '\'' +
                '}';
    }
}
