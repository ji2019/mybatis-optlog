package com.iw2f.mybatisoptlog.persistent.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClassInfo {
	private Long id;
	private Integer deviceId;
	private Integer deviceNumber;
	private String memberId;
	private Long storeId;
}