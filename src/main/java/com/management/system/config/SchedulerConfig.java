package com.management.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.management.system.schedulers.BasicScheduler;

@Configuration
@EnableScheduling
public class SchedulerConfig {

	@Value("${scheduler.enabled}")
	private boolean schedulerEnabled;

	@Autowired
	private BasicScheduler basicScheduler;

	@Scheduled(fixedRate = 30000)
	public void performScheduledTask() {
		if (schedulerEnabled) {
			basicScheduler.basicScheduler();
		}
	}
}