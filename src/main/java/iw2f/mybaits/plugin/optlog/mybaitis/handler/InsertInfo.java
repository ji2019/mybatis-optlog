package iw2f.mybaits.plugin.optlog.mybaitis.handler;

import java.util.LinkedHashMap;

import iw2f.mybaits.plugin.optlog.mybaitis.BasicInfo;
import lombok.Getter;

/**
 * 数据插入信息
 *
 */
@Getter
public class InsertInfo extends BaseDataLogHandler {

	/**
	 * 插入对象
	 */
	private LinkedHashMap<String, Object> insertObj;

	public InsertInfo(BasicInfo basicInfo, String tableName, LinkedHashMap<String, Object> insertObj) {
		super(basicInfo);
		this.insertObj = insertObj;
	}

}