package com.iw2f.mybatisoptlog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import iw2f.mybaits.plugin.optlog.interceptor.SimpleLogInterceptor;
import iw2f.mybaits.plugin.optlog.utils.LogContext;



@Component
public class LogInterceptor extends SimpleLogInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		//UserEmployee userEmployee = UserUtils.getUserEmployee();
		//if (userEmployee == null) {
			//return true;
		//}
		LogContext.setUserId("empId");
		LogContext.setUserName("EmpName");
		return super.preHandle(request, response, handler);
	}

}