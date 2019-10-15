package com.gymexpress.histar.logs.mybaitis.bo;

import com.gymexpress.histar.logs.mybaitis.BaseDataLogHandler;
import com.gymexpress.histar.logs.mybaitis.BasicInfo;

import lombok.Getter;

/**
 * 数据删除信息
 *
 */
@Getter
public class DeleteInfo extends BaseDataLogHandler {

	/**
	 * 删除对象
	 */
	private Object deleteObj;

	public DeleteInfo(BasicInfo basicInfo, Object deleteObj) {
		super(basicInfo);
		this.deleteObj = deleteObj;
	}

}