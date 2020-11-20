package com.iw2f.mybatisoptlog.persistence.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("score")
@Accessors(chain = true)
public class Score {
	@TableId(value = "score_id", type = IdType.INPUT)
	private Integer scoreId;
	private Integer courseNum;
	private Integer studentNum;
	private Integer score;
}
