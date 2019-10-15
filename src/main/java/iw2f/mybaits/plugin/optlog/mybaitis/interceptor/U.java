package iw2f.mybaits.plugin.optlog.mybaitis.interceptor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;

public interface U {
	
	Logger log = LoggerFactory.getLogger(U.class);

	static List<Map<String, Object>> getDelData(DataSource dataSource, String sql) {
		try {
			Statement stmt = CCJSqlParserUtil.parse(sql);
			if (!(stmt instanceof Delete)) {
				throw new IllegalArgumentException("");
			}
			Delete deleteStmt = (Delete) stmt;
			Table table = deleteStmt.getTable();
			String tableName = table.getName();
			Expression expression = deleteStmt.getWhere();
			String dataSql = "select * from " + tableName + " where " + expression.toString();
			QueryRunner runner = new QueryRunner(dataSource);
			List<Map<String, Object>> rs = runner.query(dataSql, new MapListHandler());
			return rs;
		} catch (JSQLParserException | SQLException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 */
	static List<Map<String, Object>> getPreUpdateData(DataSource dataSource, String sql) {
		try {
			Statement stmt = CCJSqlParserUtil.parse(sql);
			if (!(stmt instanceof Update)) {
				throw new IllegalArgumentException("");
			}
			Update updateStmt = (Update) stmt;
			List<Table> tables = updateStmt.getTables();
			String tableName = tables.get(0).getName();
			Expression expression = updateStmt.getWhere();
			String dataSql = "select * from " + tableName + " where " + expression.toString();
			QueryRunner runner = new QueryRunner(dataSource);
			List<Map<String, Object>> rs = runner.query(dataSql, new MapListHandler());
			return rs;
		} catch (JSQLParserException | SQLException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
