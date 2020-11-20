package iw2f.mybaits.plugin.optlog.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({ OptLogProperties.class })
@ConditionalOnProperty(prefix = "optlog", name = "enable", havingValue = "true")
@ConditionalOnBean({ DataSource.class,JdbcTemplate.class,DataLogHandler.class})
public class DataLogConfig {

	private final DataLogHandler dataLogHandler;

	private final DataSource dataSource;
	
	private final JdbcTemplate jdbcTemplate;

	@Bean
	@ConditionalOnMissingBean
	public OptLogStatementInterceptor optLogStatementInterceptor() {
		return new OptLogStatementInterceptor(dataSource, dataLogHandler);
	}

	@Bean
	@ConditionalOnMissingBean
	public OptLogger optLogService() {
		return new OptLogger(jdbcTemplate);
	}

}