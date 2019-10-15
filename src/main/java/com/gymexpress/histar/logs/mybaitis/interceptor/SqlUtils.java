package com.gymexpress.histar.logs.mybaitis.interceptor;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public interface SqlUtils {

	Logger log = LoggerFactory.getLogger(SqlUtils.class);

	/**
	 * 
	 * @Description 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理
	 * @author wangjc
	 * @return String
	 * @param obj
	 * @return
	 * @date 2019年10月11日 下午5:34:17
	 */
	static String getParameterValue(Object obj) {
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

	/**
	 * 
	* @Description 取得有占位符的原始SQL
	* @author wangjc
	* @return String
	* @param metaObject
	* @return
	* @date 2019年10月13日 上午8:19:40
	 */
	static String getOriginalSql(MetaObject metaObject) {
		return null;
	}
	/**
	 * 
	* @Description 取得完整SQL 进行？的替换
	* @author wangjc
	* @return String
	* @param metaObject
	* @return
	* @date 2019年10月13日 上午8:19:09
	 */
	static String getCompleteSQL(Configuration configuration, BoundSql boundSql) {
		// 获取参数
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		// sql语句中多个空格都用一个空格代替
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
			// 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			// 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
			} else {
				// MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						// 该分支是动态sql
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
					} else {
						// 打印出缺失，提醒该参数缺失并防止错位
						sql = sql.replaceFirst("\\?", "缺失");
					}
				}
			}
		}
		return sql;
	}
	
	/**
	 * 
	* @Description 取得SQL参数，配合 getOriginalSql
	* @author wangjc
	* @return String
	* @param metaObject
	* @return
	* @date 2019年10月13日 上午8:19:09
	 */
	static List<Object> getSqlParams(Configuration configuration, BoundSql boundSql) {
		List<Object> params = new ArrayList<Object>();
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
	
}
