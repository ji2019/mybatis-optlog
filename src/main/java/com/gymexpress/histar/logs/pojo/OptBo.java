package com.gymexpress.histar.logs.pojo;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptBo {
	
	private String desc;
	
	private Object data;

	@Override
	public String toString() {
		return desc + " " + JSON.toJSONString(data);
	}
	
}
