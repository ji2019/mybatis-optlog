package com.gymexpress.histar.logs.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public interface Utils {

	static final String EMPTY = "";

	static String id() {
		String s = java.util.UUID.randomUUID().toString();
		return s.replace("-", "");
	}

	static String getRemoteAddr(HttpServletRequest request) {
		if (request == null) {
			return "unknown";
		}
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		ip = StringUtils.hasText(ip) ? StringUtils.trimWhitespace(ip) : EMPTY;
		return StringUtils.split(ip, ",")[0];
	}
}
