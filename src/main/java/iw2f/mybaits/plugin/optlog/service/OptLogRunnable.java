package iw2f.mybaits.plugin.optlog.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import iw2f.mybaits.plugin.optlog.mybaitis.bo.OptLogBo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OptLogRunnable implements Runnable {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final DataSource dataSource;

	private final List<OptLogBo> optLogs;

	@Override
	public void run() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			String sql = "insert into opt_log (log_id,user_id,user_name,user_ip,method,jmethod_desc,jmethod,param,create_time,request_url,response_status,opts) values (?,?,?,?,?,?,?,?,?,?,?,?)";
			QueryRunner runner = new QueryRunner();
			OptLogBo[] optLogArray = new OptLogBo[optLogs.size()];
			optLogArray = optLogs.toArray(new OptLogBo[] {});
			Object[][] params = new Object[optLogs.size()][12];
			for (int i = 0; i < optLogArray.length; i++) {
				OptLogBo optLog = optLogArray[i];
				params[i][0] = optLog.getLogId();// log_id
				params[i][1] = optLog.getUserId();// user_id
				params[i][2] = optLog.getUserName();// user_name
				params[i][3] = optLog.getUserIp();// user_ip
				params[i][4] = optLog.getMethod();// method
				params[i][5] = optLog.getJmethodDesc();// jmethod_desc
				params[i][6] = optLog.getJmethod();// jmethod
				params[i][7] = optLog.getParam();// param
				params[i][8] = optLog.getCreateTime();// create_time
				params[i][9] = optLog.getRequestUrl();// request_url
				params[i][10] = optLog.getResponseStatus();// response_status
				params[i][11] = JSON.toJSONString(optLog.getOpts());// opts
				runner.update(conn, sql, params[i]);
			}
		} catch (SQLException e) {
			logger.info(e.getMessage(), e);
		} finally {
			DbUtils.commitAndCloseQuietly(conn);
		}
	}

}