package com.iw2f.mybatisoptlog.persistence.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Course {
	private Integer courseNum;
	private String courseName;
	private Integer courseHour;
	private Integer courseScore;
}