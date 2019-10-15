package com.gymexpress.histar.logs.mybaitis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.util.StringUtils;

import com.gymexpress.histar.logs.mybaitis.bo.FieldInfo;

import lombok.Getter;

/**
 * 基础信息
 *
 */
@Getter
public class BasicInfo {

	private static ConcurrentHashMap<String, List<FieldInfo>> fields = new ConcurrentHashMap<>();

	/**
	 * 数据源
	 */
	private DataSource dataSource;

	/**
	 * 表名
	 */
	private String tbName;

	/**
	 * 表字段注释
	 */
	private List<FieldInfo> fieldInfos;

	public BasicInfo(DataSource dataSource, String tbName) {
		this.dataSource = dataSource;
		this.tbName = tbName;
	}

	/**
	 * 
	 * @Description TODO
	 * @author wangjc
	 * @return List<FieldInfo>
	 * @return
	 * @date 2019年10月11日 下午5:35:43
	 */
	public List<FieldInfo> getFieldInfos() {
		if (!fields.containsKey(this.tbName)) {
			String query = "select column_name fieldName, column_comment comment ,data_type dataType from information_schema.columns"
					+ " where table_name = \"" + this.tbName + "\" and table_schema = (select database())";
			try {
				QueryRunner runner = new QueryRunner(dataSource);
				this.fieldInfos = runner.query(query, new BeanListHandler<FieldInfo>(FieldInfo.class));
			} catch (SQLException e) {
				this.fieldInfos = new ArrayList<>();
			}
			this.fieldInfos.forEach(f -> {
				// String caseName = this.columnToJava(f.getFieldName());
				// f.setJFieldName(StringUtils.uncapitalize(caseName));
				f.setJFieldName(StringUtils.uncapitalize(f.getFieldName()));
			});
			fields.put(this.tbName, this.fieldInfos);
		}
		return fields.get(this.tbName);
	}

}