package com.iw2f.mybatisoptlog.persistence.dao;

import java.util.List;

import com.iw2f.mybatisoptlog.persistence.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {
    void insert(Course o);
    List<Course> selectAll();
    void updateById(Course o);
}