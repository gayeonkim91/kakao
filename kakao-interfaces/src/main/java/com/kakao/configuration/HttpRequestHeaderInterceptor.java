package com.kakao.configuration;

import com.kakao.interfaces.common.context.ClientContext;
import com.kakao.interfaces.common.context.ClientContextHolder;
import com.kakao.interfaces.common.context.ContextGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class HttpRequestHeaderInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		ClientContext context = ContextGenerator.generateContext(request);
		ClientContextHolder.set(context);
		return super.preHandle(request, response, handler);
	}
}
