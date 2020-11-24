package iw2f.mybaits.plugin.optlog.mybaitis.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import iw2f.mybaits.plugin.optlog.mybaitis.bo.FieldInfo;
import lombok.Getter;

/**
 * 基础信息
 */
@Getter
public class BasicInfo {

	private JdbcTemplate jdbcTemplate;

	private static ConcurrentHashMap<String, List<FieldInfo>> fields = new ConcurrentHashMap<>();

	private static final String query_column_sql = "select column_name , column_comment ,data_type from information_schema.columns  where table_name = ? and table_schema = (select database())";

	private static final String query_PRIMARY_KEY = "SELECT CONSTRAINT_SCHEMA, CONSTRAINT_NAME, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME  FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE  WHERE table_schema = (SELECT DATABASE())  AND table_name = ?  AND CONSTRAINT_NAME = 'PRIMARY';";

	/**
	 * 表名
	 */
	private String tbName;

	/**
	 * 表字段注释
	 */
	private List<FieldInfo> fieldInfos;

	public BasicInfo(JdbcTemplate jdbcTemplate, String tbName) {
		this.jdbcTemplate = jdbcTemplate;
		this.tbName = tbName;
	}

	/**
	 * @return
	 * @Description TODO
	 * @author wangjc
	 * @date 2019年10月11日 下午5:35:43
	 */
	public List<FieldInfo> getFieldInfos() {
		if (!fields.containsKey(this.tbName)) {
			List<Map<String, Object>> PRIMARY_KEYs = jdbcTemplate.queryForList(query_PRIMARY_KEY, this.tbName);
			Set<String> sets = PRIMARY_KEYs.stream().map(it -> it.get("COLUMN_NAME")).map(it -> it.toString()).collect(Collectors.toSet());
			List<Map<String, Object>> column_map = jdbcTemplate.queryForList(query_column_sql, this.tbName);
			this.fieldInfos = column_map.stream().map(it -> {
				String column_name = (String)it.get("column_name");
				 FieldInfo p = new FieldInfo();
				p.setComment((String)it.get("column_comment"));
				p.setFieldName(column_name);
				p.setDataType((String)it.get("data_type"));
				p.setJFieldName(StringUtils.uncapitalize(column_name));
				p.setPrimaryKey(sets.contains(column_name));
				return  p;
			}).collect(Collectors.toList());
			fields.put(this.tbName, this.fieldInfos);
		}
		return fields.get(this.tbName);
	}

}