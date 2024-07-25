package com.management.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.management.system.repository.UserActivityLogRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.management.system.entity.UserActivityLog;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserActivityInterceptor implements HandlerInterceptor {

	@Autowired
	private UserActivityLogRepository userActivityLogRepository;

	@Autowired
	private UserActivityConfig userActivityConfig;

	private static final Logger logger = LoggerFactory.getLogger(UserActivityInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!userActivityConfig.isUserActivityLoggingEnabled()) {
			return true;
		}
		request.setAttribute("startTime", System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (!userActivityConfig.isUserActivityLoggingEnabled()) {
			return;
		}
		String username = getUsername();
		String uri = request.getRequestURI();
		String method = request.getMethod();
		LocalDateTime timestamp = LocalDateTime.now();
		String ipAddress = getClientIpAddress(request);
		int status = response.getStatus();
		boolean success = status >= 200 && status < 400;

		UserActivityLog log = new UserActivityLog();
		log.setUsername(username);
		log.setAction(method);
		log.setDetails(uri);
		log.setTimestamp(timestamp);
		log.setIpAddress(ipAddress);
		log.setStatus(success ? "SUCCESS" : "FAILURE");

		userActivityLogRepository.save(log);

		logger.info("User '{}' accessed URI: '{}' with method '{}' from IP '{}' with status '{}'", username, uri,
				method, ipAddress, success ? "SUCCESS" : "FAILURE");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (!userActivityConfig.isUserActivityLoggingEnabled()) {
			return;
		}
		if (ex != null) {
			String username = getUsername();
			String uri = request.getRequestURI();
			String method = request.getMethod();
			LocalDateTime timestamp = LocalDateTime.now();
			String ipAddress = getClientIpAddress(request);

			UserActivityLog log = new UserActivityLog();
			log.setUsername(username);
			log.setAction(method);
			log.setDetails(uri);
			log.setTimestamp(timestamp);
			log.setIpAddress(ipAddress);
			log.setStatus("FAILURE");
			log.setErrorDetails(ex.getMessage());

			userActivityLogRepository.save(log);

			logger.error("User '{}' accessed URI: '{}' with method '{}' from IP '{}' and encountered error: '{}'",
					username, uri, method, ipAddress, ex.getMessage());
		}
	}

	private String getUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
		}
	}

	private String getClientIpAddress(HttpServletRequest request) {
		String remoteAddr = "";

		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}

		return remoteAddr;
	}

}