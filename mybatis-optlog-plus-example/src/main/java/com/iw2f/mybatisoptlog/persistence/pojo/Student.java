package com.iw2f.mybatisoptlog.persistence.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("student")
@Accessors(chain = true)
public class Student {
	@TableId(value = "student_num", type = IdType.INPUT)
	private Integer studentNum;
	private String studentName;
	private String studentSex;
	private String studentBirthday;
}
