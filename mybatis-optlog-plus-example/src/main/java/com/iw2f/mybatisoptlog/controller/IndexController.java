package com.iw2f.mybatisoptlog.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iw2f.mybatisoptlog.persistence.dao.CourseMapper;
import com.iw2f.mybatisoptlog.persistence.pojo.Course;

import iw2f.mybaits.plugin.optlog.annotation.ControllerLogs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class IndexController {

	private static AtomicInteger atomicInteger = new AtomicInteger(1);

	@Autowired
	private CourseMapper courseMapper;

	@ControllerLogs("编辑课程")
	@GetMapping("/index")
	public Object index() {
		QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
		List<Course> courses = courseMapper.selectList(queryWrapper);
		String str = JSON.toJSONStringWithDateFormat(courses, "yyyy-MM-dd HH:mm:ss", SerializerFeature.PrettyFormat,
				SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
		for (Course course : courses) {
			int i = atomicInteger.getAndIncrement();
			course.setCourseHour(i);
			courseMapper.updateById(course);
		}
		log.info(str);
		return courses;
	}
}
