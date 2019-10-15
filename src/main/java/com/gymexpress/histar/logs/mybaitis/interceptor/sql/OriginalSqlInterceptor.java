package com.gymexpress.histar.logs.mybaitis.interceptor.sql;

import java.sql.Connection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gymexpress.histar.logs.mybaitis.interceptor.utils.PluginUtil;

/**
 * 
 * @author wangjc
 * @date 2019年10月11日 下午5:33:00
 *
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class OriginalSqlInterceptor implements Interceptor {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.
	 * Invocation)
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		String interceptMethod = invocation.getMethod().getName();
		if (!"prepare".equals(interceptMethod)) {
			return invocation.proceed();
		}
		StatementHandler handler = (StatementHandler) PluginUtil.processTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(handler);
		// 获取xml中的一个select/update/insert/delete节点，是一条SQL语句
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
		// 获取原始sql
		String originalSql = (String) metaObject.getValue("delegate.boundSql.sql");
		String sqlId = mappedStatement.getId(); // 获取到节点的id,即sql语句的id
		log.debug(sqlId + "==> : " + originalSql);
		Configuration configuration = mappedStatement.getConfiguration(); // 获取节点的配置
		List<String> params = showSqlParams(configuration, boundSql); // 获取到最终的sql语句
		log.debug("params : " + params);
		return invocation.proceed();
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
	 * 
	 * @Description 进行？的替换
	 * @author wangjc
	 * @return List<String>
	 * @param configuration
	 * @param boundSql
	 * @return
	 * @date 2019年10月11日 下午5:32:17
	 */
	public static List<String> showSqlParams(Configuration configuration, BoundSql boundSql) {
		List<String> params = new ArrayList<String>();
		// 获取参数
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		// sql语句中多个空格都用一个空格代替
		if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
			// 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			// 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				params.add(Matcher.quoteReplacement(getParameterValue(parameterObject)));
			} else {
				// MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						params.add(Matcher.quoteReplacement(getParameterValue(obj)));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						// 该分支是动态sql
						Object obj = boundSql.getAdditionalParameter(propertyName);
						params.add(Matcher.quoteReplacement(getParameterValue(obj)));
					} else {
						params.add(Matcher.quoteReplacement(getParameterValue("缺失")));
					}
				}
			}
		}
		return params;
	}

	/**
	 * 
	 * @Description 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理
	 * @author wangjc
	 * @return String
	 * @param obj
	 * @return
	 * @date 2019年10月11日 下午5:32:39
	 */
	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}
		}
		return value;
	}
}
