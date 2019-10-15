package com.gymexpress.histar.logs.mybaitis.bo;

import java.util.LinkedHashMap;

import com.gymexpress.histar.logs.mybaitis.BaseDataLogHandler;
import com.gymexpress.histar.logs.mybaitis.BasicInfo;

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

	public InsertInfo(BasicInfo basicInfo,String tableName,LinkedHashMap<String, Object> insertObj) {
		super(basicInfo);
		this.insertObj = insertObj;
	}

}