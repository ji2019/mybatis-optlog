package iw2f.mybaits.plugin.optlog.mybaitis.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

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
	 * @return
	 * @Description TODO
	 * @author wangjc
	 * @date 2019年10月11日 下午5:35:43
	 */
	public List<FieldInfo> getFieldInfos() {
		if (!fields.containsKey(this.tbName)) {
			String query = "select column_name fieldName, column_comment comment ,data_type dataType from information_schema.columns"
					+ " where table_name = \"" + this.tbName + "\" and table_schema = (select database())";
			this.fieldInfos = jdbcTemplate.query(query, new RowMapper<FieldInfo>() {
				@Override
				public FieldInfo mapRow(ResultSet arg0, int arg1) throws SQLException {
					FieldInfo p = new FieldInfo();
					p.setComment(arg0.getString("comment"));
					p.setFieldName(arg0.getString("fieldName"));
					p.setDataType(arg0.getString("dataType"));
					return p;
				}
			});
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