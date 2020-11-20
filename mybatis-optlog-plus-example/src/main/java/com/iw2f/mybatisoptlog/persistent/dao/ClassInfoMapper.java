package com.iw2f.mybatisoptlog.persistent.dao;

import java.util.List;

import com.iw2f.mybatisoptlog.persistent.pojo.ClassInfo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ClassInfoMapper {

	void insert(ClassInfo o);

	List<ClassInfo> selectAll();

	void updateById(ClassInfo hd);
}