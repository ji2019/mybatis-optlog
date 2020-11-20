package iw2f.mybaits.plugin.optlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import iw2f.mybaits.plugin.optlog.mybaitis.handler.DataLogHandler;
import iw2f.mybaits.plugin.optlog.mybaitis.interceptor.logs.OptLogStatementInterceptor;
import iw2f.mybaits.plugin.optlog.properties.OptLogProperties;
import iw2f.mybaits.plugin.optlog.service.OptLogger;
import lombok.AllArgsConstructor;

/**
 * 
 * @Description 数据更新日志处理配置（实现按需加载）
 * @author wangjc
 * @date 2019年10月13日 下午12:00:22
 *
 */
@ComponentScan(basePackages = {"iw2f.mybaits.plugin.optlog"})
@AllArgsConstructor
@EnableConfigurationProperties({ OptLogProperties.class })
@ConditionalOnProperty(prefix = "optlog", name = "enable", havingValue = "true")
public class OptLogAutoConfigure {

	@Autowired
	private final DataLogHandler dataLogHandler;
	
	@Autowired
	private final JdbcTemplate jdbcTemplate;

	@Bean
	@ConditionalOnMissingBean
	public OptLogStatementInterceptor optLogStatementInterceptor() {
		return new OptLogStatementInterceptor(jdbcTemplate, dataLogHandler);
	}

	@Bean
	@ConditionalOnMissingBean
	public OptLogger optLogger() {
		return new OptLogger(jdbcTemplate);
	}

}