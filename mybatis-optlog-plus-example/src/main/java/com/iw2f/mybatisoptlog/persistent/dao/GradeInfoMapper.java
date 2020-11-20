package com.iw2f.mybatisoptlog.persistent.dao;

import java.util.List;

import com.iw2f.mybatisoptlog.persistent.pojo.GradeInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GradeInfoMapper {
	
	  void insert(GradeInfo o);

	  void insertListBatch(List<GradeInfo> os);
}