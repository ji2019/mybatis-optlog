package com.iw2f.mybatisoptlog.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.iw2f.mybatisoptlog.interceptor.LogInterceptor;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
   
	@Autowired
	private LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
        super.addInterceptors(registry);
    }
}