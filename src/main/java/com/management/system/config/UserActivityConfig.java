package com.management.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserActivityConfig {

    @Value("${user.activity.logging.enabled}")
    private boolean userActivityLoggingEnabled;

    public boolean isUserActivityLoggingEnabled() {
        return userActivityLoggingEnabled;
    }
}