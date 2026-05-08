package com.xzzj.medmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置类
 * 启用Spring的定时任务支持
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
}