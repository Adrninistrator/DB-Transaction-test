package test.db.util;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2022/6/23
 * @description:
 */
public class DbUtil {
    private static final Logger logger = LoggerFactory.getLogger(DbUtil.class);

    /**
     * 查询一行记录
     *
     * @param sql
     * @param arguments
     * @return
     */
    public static Map<String, Object> queryOneRow(Connection connection, String sql, Object[] arguments) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(sql);
            setArguments(stmt, arguments);
            rs = stmt.executeQuery();

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            Map<String, Object> map = new HashMap<>(columnCount);
            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    /*
                        当查询SQL通过AS指定字段别名时，使用getColumnLabel可以获取到别名，未指定别名时，可获取到原始字段名
                        使用getColumnName只能获取到原始字段名
                     */
                    map.put(meta.getColumnLabel(i), rs.getObject(i));
                }
            }
            return map;
        } catch (Exception e) {
            return null;
        } finally {
            // 在Druid filter中获取到的链接，不能关闭，否则会影响后续使用
            close(connection, stmt, false);
            closeResultSet(rs);
        }
    }

    private static void setArguments(PreparedStatement stmt, Object[] arguments) throws SQLException {
        if (arguments != null) {
            int argumentNum = arguments.length;
            for (int i = 0; i < argumentNum; i++) {
                stmt.setObject(i + 1, arguments[i]);
            }
        }
    }

    private static void close(Connection connection, PreparedStatement stmt) {
        close(connection, stmt, true);
    }

    private static void close(Connection connection, PreparedStatement stmt, boolean closeConnection) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (closeConnection && connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            logger.error("error ", e);
        }
    }

    private static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            logger.error("error ", e);
        }
    }

    public static void restartDataSources(ApplicationContext applicationContext) {
        Map<String, DruidDataSource> druidDataSourceMap = applicationContext.getBeansOfType(DruidDataSource.class);
        if (druidDataSourceMap.isEmpty()) {
            logger.error("druidDataSourceMap is empty");
            return;
        }

        for (Map.Entry<String, DruidDataSource> entry : druidDataSourceMap.entrySet()) {
            DruidDataSource druidDataSource = entry.getValue();

            if (druidDataSource != null) {
                logger.info("druidDataSource.restart {}", entry.getKey());

                try {
                    druidDataSource.restart();
                } catch (SQLException e) {
                    logger.error("error ", e);
                }
            }
        }
    }

    private DbUtil() {
        throw new IllegalStateException("illegal");
    }
}
