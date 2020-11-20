package com.iw2f.mybatisoptlog.persistence.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

//@KeySequence("SEQ_USER")

@Data
@TableName("course")
@Accessors(chain = true)
public class Course {
	@TableId(value = "course_num", type = IdType.INPUT)
	private Integer courseNum;
	private String courseName;
	private Integer courseHour;
	private Integer courseScore;
	@TableField(exist = false)
	private String ignoreColumn = "ignoreColumn";
	@TableField(exist = false)
	private Integer count;
}