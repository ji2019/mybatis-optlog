package com.iw2f.mybatisoptlog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import  iw2f.mybaits.plugin.optlog.interceptor.*;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;



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
		//LogContext.setUserId(userEmployee.getEmpId() + "");
		//LogContext.setUserName(userEmployee.getEmpName());
		return super.preHandle(request, response, handler);
	}

}