package iw2f.mybaits.plugin.optlog.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import iw2f.mybaits.plugin.optlog.mybaitis.bo.OptLogBo;

@Service
public class OptLogger {

	private final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	public OptLogger(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	public void add(List<OptLogBo> optLogs) {
		service.execute(new OptLogRunnable(optLogs,jdbcTemplate));
	}

}
