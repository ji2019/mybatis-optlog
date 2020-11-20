package com.iw2f.mybatisoptlog.controller;

import com.alibaba.fastjson.JSON;
import com.iw2f.mybatisoptlog.persistence.dao.CourseMapper;
import com.iw2f.mybatisoptlog.persistence.pojo.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class IndexController {

    @Autowired
    private CourseMapper courseMapper;

    //@PostMapping("/test")
    //@ResponseBody

    @GetMapping("/index")
    @ResponseBody
    public String getString() {
        List<Course> courses =  courseMapper.selectAll();
        String str = JSON.toJSONString(courses);
        log.info(str);
        return "";
    }
}
