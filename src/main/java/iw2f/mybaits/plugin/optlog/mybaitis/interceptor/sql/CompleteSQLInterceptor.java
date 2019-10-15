package iw2f.mybaits.plugin.optlog.mybaitis.interceptor.sql;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iw2f.mybaits.plugin.optlog.mybaitis.interceptor.SqlUtils;

/**
 * 
 * @Description 完整SQL，包括参数替换
 * @author wangjc
 * @date 2019年10月13日 上午9:20:21
 *
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public class CompleteSQLInterceptor implements Interceptor {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.
	 * Invocation)
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 获取xml中的一个select/update/insert/delete节点，是一条SQL语句
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		// 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
			log.info("parameter = " + parameter);
		}
		String sqlId = mappedStatement.getId(); // 获取到节点的id,即sql语句的id
		BoundSql boundSql = mappedStatement.getBoundSql(parameter); // BoundSql就是封装myBatis最终产生的sql类
		Configuration configuration = mappedStatement.getConfiguration(); // 获取节点的配置
		String sql = getSql(configuration, boundSql, sqlId); // 获取到最终的sql语句
		log.info(sqlId + "==> : " + sql);
		// 执行完上面的任务后，不改变原有的sql执行过程
		return invocation.proceed();
	}

	/**
	 * 
	 * @Description 封装了一下sql语句，使得结果返回完整xml路径下的sql语句节点id + sql语句
	 * @author wangjc
	 * @return String
	 * @param configuration
	 * @param boundSql
	 * @param sqlId
	 * @return
	 * @date 2019年10月11日 下午5:34:04
	 */
	public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
		String sql = SqlUtils.getCompleteSQL(configuration, boundSql);
		StringBuilder str = new StringBuilder(100);
		str.append(sqlId);
		str.append(":");
		str.append(sql);
		return str.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties properties) {

	}

	// import net.sf.jsqlparser.JSQLParserException;
	// import net.sf.jsqlparser.expression.Expression;
	// import net.sf.jsqlparser.parser.CCJSqlParserUtil;
	// import net.sf.jsqlparser.schema.Column;
	// import net.sf.jsqlparser.schema.Table;
	// import net.sf.jsqlparser.statement.Statement;
	// import net.sf.jsqlparser.statement.delete.Delete;
	// import net.sf.jsqlparser.statement.drop.Drop;
	// import net.sf.jsqlparser.statement.insert.Insert;
	// import net.sf.jsqlparser.statement.select.Select;
	// import net.sf.jsqlparser.statement.select.SelectBody;
	// import net.sf.jsqlparser.statement.update.Update;

	/***************************************************************************************/

	// public void test(String sql) throws JSQLParserException {
	// Statement stmt = CCJSqlParserUtil.parse(sql);
	// if ((stmt instanceof Update)) {
	// Update updateStatement = (Update) stmt;
	// List<Column> update_columns = updateStatement.getColumns();
	// List<Expression> update_values = updateStatement.getExpressions();
	// Map<String, String> obj = new HashMap<String, String>();
	// for (int i = 0; i < update_columns.size(); i++) {
	// String column_name = update_columns.get(i).toString();
	// String column_value = update_values.get(i).toString();
	// obj.put(column_name, column_value);
	// }
	// } else if (stmt instanceof Insert) {
	// Insert insert = (Insert) stmt;
	// List<Column> columns = insert.getColumns();
	// for (Column col : columns) {
	// col.getColumnName();
	// col.getASTNode().jjtGetValue();
	// }
	// } else if (stmt instanceof Drop) {
	// Delete drop = (Delete) stmt;
	// Table table = drop.getTable();
	// } else if (stmt instanceof Select) {
	// Select select = (Select) stmt;
	// SelectBody selectBody = select.getSelectBody();
	// }
	// }
}