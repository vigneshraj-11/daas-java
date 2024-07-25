package com.management.system.schedulers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class BasicScheduler {

	public void basicScheduler() {
		System.out.println("Scheduled task executed at " + LocalDateTime.now());
	}
}
