package com.iw2f.mybatisoptlog.persistence.dao;

import com.iw2f.mybatisoptlog.persistence.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    void insert(Student o);
    List<Student> selectAll();
    void updateById(Student o);
}