package com.love;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

@Slf4j
public class FilterLog extends SimplePageCachingFilter {
	
	private void log(HttpServletRequest request) {
		Map headers = new HashMap();
		Enumeration enumeration = request.getHeaderNames();
		StringBuffer logLine = new StringBuffer();
		logLine.append("Request Headers");
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			String headerValue = request.getHeader(name);
			headers.put(name, headerValue);
			logLine.append(": ").append(name).append(" -> ").append(headerValue);
		}
		log.info("【浏览者信息】" + logLine.toString() + "RemoteAddr->" + request.getRemoteAddr());
	}
	
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws AlreadyGzippedException, AlreadyCommittedException, FilterNonReentrantException, LockTimeoutException, Exception {
		log(request);// 记录访问日志
		super.doFilter(request, response, chain);
	}
}
