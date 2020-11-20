package iw2f.mybaits.plugin.optlog.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import iw2f.mybaits.plugin.optlog.LogContext;
import iw2f.mybaits.plugin.optlog.annotation.ControllerLogs;
import iw2f.mybaits.plugin.optlog.mybaitis.bo.OptBo;
import iw2f.mybaits.plugin.optlog.mybaitis.bo.OptLogBo;
import iw2f.mybaits.plugin.optlog.service.OptLogger;
import iw2f.mybaits.plugin.optlog.utils.Utils;

public class SimpleLogInterceptor implements HandlerInterceptor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String LOGGER_ENTITY = "_logger_bo";

	@Autowired
	private OptLogger optLogger;

	/**
	 * before entering Controller
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		String controllerName = handlerMethod.getBeanType().getName();
		Method method = handlerMethod.getMethod();
		if (!method.isAnnotationPresent(ControllerLogs.class)) {
			return true;
		}
		ControllerLogs controllerLogs = method.getAnnotation(ControllerLogs.class);
		String methodName = handlerMethod.getMethod().getName();
		OptLogBo log = new OptLogBo();
		log.setLogId(Utils.id());
		log.setJmethod(controllerName + "." + methodName);
		log.setUserName(LogContext.getUserName());
		log.setUserId(LogContext.getUserId());
		log.setUserIp(Utils.getRemoteAddr(request));
		log.setJmethodDesc(controllerLogs.description());
		log.setCreateTime(new Date());
		log.setRequestUrl(request.getRequestURI());
		log.setMethod(request.getMethod());
		log.setParam(JSON.toJSONString(request.getParameterMap(), SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteMapNullValue));
		LogContext.optLog(log);
		LogContext.record(true);
		// 设置请求实体到request内，方便afterCompletion方法调用
		request.setAttribute(LOGGER_ENTITY, log);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object handler, ModelAndView modelAndView) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return;
		}
		// int status = httpServletResponse.getStatus();
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
			throws Exception {
		if (!LogContext.record()) {
			return;
		}
		try {
			OptLogBo optLog = LogContext.optLog();
			String logId = optLog.getLogId();
			logger.debug("          " + logId);
			int status = response.getStatus();
			optLog.setResponseStatus(status);
			for (OptBo str : optLog.getOpts()) {
				logger.debug("          =====================" + optLog.getUserName() + " " + str);
			}
			// 执行将日志写入数据库
			optLogger.add(Arrays.asList(new OptLogBo[] { optLog }));
		} catch (Exception e2) {
			logger.error(e2.getMessage(), e2);
		}

	}

}
