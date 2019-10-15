package com.gymexpress.histar.logs.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import com.gymexpress.histar.logs.pojo.OptLogBo;

public class OptLogger {

	private final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private final DataSource dataSource;

	public OptLogger(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public void add(List<OptLogBo> optLogs) {
		service.execute(new OptLogRunnable(dataSource, optLogs));
	}

}
