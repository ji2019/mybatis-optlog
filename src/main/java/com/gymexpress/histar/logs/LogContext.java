package com.gymexpress.histar.logs;

import com.gymexpress.histar.logs.pojo.OptLogBo;

public class LogContext {

	private static ThreadLocal<Boolean> record = new ThreadLocal<Boolean>() {
		public Boolean initialValue() {
			return false;
		}
	};

	private static ThreadLocal<OptLogBo> optLog = new ThreadLocal<OptLogBo>();

	private static ThreadLocal<String> userId = new ThreadLocal<String>();

	private static ThreadLocal<String> userName = new ThreadLocal<String>();

	public static Boolean record() {
		return record.get();
	}

	public static void record(Boolean log) {
		LogContext.record.set(log);
	}

	public static OptLogBo optLog() {
		return optLog.get();
	}

	public static void optLog(OptLogBo log) {
		LogContext.optLog.set(log);
	}

	public static String getUserId() {
		return userId.get();
	}

	public static void setUserId(String userId) {
		LogContext.userId.set(userId);
	}

	public static String getUserName() {
		return userName.get();
	}

	public static void setUserName(String userName) {
		LogContext.userName.set(userName);
	}

}
