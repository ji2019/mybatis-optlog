package iw2f.mybaits.plugin.optlog.mybaitis.interceptor.logs;

import java.io.StringReader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;

import iw2f.mybaits.plugin.optlog.mybaitis.handler.BasicInfo;
import iw2f.mybaits.plugin.optlog.mybaitis.handler.DataLogHandler;
import iw2f.mybaits.plugin.optlog.mybaitis.handler.DeleteInfo;
import iw2f.mybaits.plugin.optlog.mybaitis.handler.InsertInfo;
import iw2f.mybaits.plugin.optlog.mybaitis.handler.UpdateInfo;
import iw2f.mybaits.plugin.optlog.mybaitis.interceptor.utils.DataUtils;
import iw2f.mybaits.plugin.optlog.mybaitis.interceptor.utils.PluginUtil;
import iw2f.mybaits.plugin.optlog.mybaitis.interceptor.utils.SqlUtils;
import iw2f.mybaits.plugin.optlog.utils.LogContext;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

/**
 * @author wangjc
 * @date 2019年10月11日 下午5:33:00
 */
@AllArgsConstructor
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class OptLogStatementInterceptor implements Interceptor {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JdbcTemplate jdbcTemplate;

    private final DataLogHandler dataLogHandler;

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.
     * Invocation)
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            StatementHandler handler = (StatementHandler) PluginUtil.processTarget(invocation.getTarget());
            MetaObject metaObject = SystemMetaObject.forObject(handler);
            MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            // 获取xml中的一个select/update/insert/delete节点，是一条SQL语句
            // MappedStatement mappedStatement = (MappedStatement)
            // metaObject.getValue("delegate.mappedStatement");
            // BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");//
            // BoundSql就是封装myBatis最终产生的sql类
            // Configuration configuration = mappedStatement.getConfiguration(); // 获取节点的配置
            SqlCommandType sqlCmdType = ms.getSqlCommandType();
            if (sqlCmdType == SqlCommandType.UPDATE || sqlCmdType == SqlCommandType.INSERT
                    || sqlCmdType == SqlCommandType.DELETE) {
                boolean record = LogContext.record();
                if (record) {
                    this.dealData(metaObject);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return invocation.proceed();

    }

    /**
     * @param metaObject
     * @return void
     * @Description 对数据库操作传入参数进行处理
     * @author wangjc
     * @date 2019年10月11日 下午5:31:38
     */
    public void dealData(MetaObject metaObject) {
        // 获取xml中的一个select/update/insert/delete节点，是一条SQL语句
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");// BoundSql就是封装myBatis最终产生的sql类
        // 获取原始sql
        Configuration configuration = mappedStatement.getConfiguration(); // 获取节点的配置
        String sql = SqlUtils.getCompleteSQL(configuration, boundSql);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        //因为替换后会出现有的值为空。SQL解析错误。就用原生的SQL了
        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
            sql = boundSql.getSql();
        }
        log.info("sql ===>> " + sql);
        List<Object> params = SqlUtils.getSqlParams(configuration, boundSql);
        log.info("params ===>> " + params);
        // 参数
        this.doLog(mappedStatement, sql, params);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
     */
    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * @param mappedStatement
     * @param sql
     * @param params
     * @return void
     * @Description 根据不同参数及操作进行不同的日志记录
     * @author wangjc
     * @date 2019年10月11日 下午5:31:06
     */
    public void doLog(MappedStatement mappedStatement, String sql, List<Object> params) {
        try {
            // DataSource dataSource =
            // mappedStatement.getConfiguration().getEnvironment().getDataSource();
            Statement stmt = CCJSqlParserUtil.parse(new StringReader(sql));
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            // 插入
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                if (stmt instanceof Insert) {
                    Insert insert = (Insert) stmt;
                    String tableName = insert.getTable().getName();
                    List<Column> columns = insert.getColumns();
                    LinkedHashMap<String, Object> insertData = new LinkedHashMap<String, Object>();
                    for (int i = 0; i < columns.size(); i++) {
                        String columnName = columns.get(i).getColumnName();
                        Object columnValue = params.get(i);
                        insertData.put(columnName, columnValue);
                    }
                    InsertInfo insertInfo = new InsertInfo(new BasicInfo(jdbcTemplate, tableName), tableName, insertData);
                    dataLogHandler.insertHandler(insertInfo);
                    System.out.println(JSON.toJSONString(insertData));
                }
                // 更新
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                if ((stmt instanceof Update)) {
                    Update updateStatement = (Update) stmt;
                    String tableName = updateStatement.getTables().get(0).getName();
                    List<Column> update_columns = updateStatement.getColumns();
                    Map<String, Object> updateData = new HashMap<String, Object>();
                    for (int i = 0; i < update_columns.size(); i++) {
                        String column_name = update_columns.get(i).getColumnName();
                        Object column_value = params.get(i);
                        updateData.put(column_name, column_value);
                    }
                    // 更新之前数据
                    List<Map<String, Object>> preUpdateData = DataUtils.getPreUpdateData(jdbcTemplate, sql);
                    if (!preUpdateData.isEmpty()) {
                        UpdateInfo updateInfo = new UpdateInfo(new BasicInfo(jdbcTemplate, tableName), preUpdateData, updateData);
                        // 调用自定义处理方法
                        dataLogHandler.updateHandler(updateInfo);
                    }
                }
                // 删除
            } else if (SqlCommandType.DELETE.equals(sqlCommandType)) {
                if (stmt instanceof Delete) {
                    Delete drop = (Delete) stmt;
                    String tableName = drop.getTable().getName();
                    List<Map<String, Object>> delObj = DataUtils.getDelData(jdbcTemplate, sql);
                    if (!delObj.isEmpty()) {
                        DeleteInfo deleteInfo = new DeleteInfo(new BasicInfo(jdbcTemplate, tableName), delObj);
                        // 调用自定义处理方法
                        dataLogHandler.deleteHandler(deleteInfo);
                    }
                }
            }
            // BoundSql boundSql = null;
            // StatementDeParser deParser = new StatementDeParser(new StringBuilder());
            // stmt.accept(deParser);
            // sql = deParser.getBuffer().toString();
            // ReflectionUtils.setFieldValue(boundSql, "sql", sql);

        } catch (JSQLParserException e) {
            log.info(e.getMessage(), e);
        }
    }

}
