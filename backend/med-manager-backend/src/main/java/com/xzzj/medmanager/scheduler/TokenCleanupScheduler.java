package com.xzzj.medmanager.scheduler;

import com.xzzj.medmanager.common.utils.TokenBlacklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Token黑名单定时清理任务
 * 定期清理已过期的Token，防止黑名单无限增长
 */
@Component
public class TokenCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TokenCleanupScheduler.class);

    @Autowired
    private TokenBlacklist tokenBlacklist;

    /**
     * 每小时执行一次Token清理任务
     * cron表达式: "0 0 * * * ?" 表示每小时整点执行
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void cleanupExpiredTokens() {
        int beforeSize = tokenBlacklist.getBlacklistSize();
        
        tokenBlacklist.removeExpiredTokens();
        
        int afterSize = tokenBlacklist.getBlacklistSize();
        int removedCount = beforeSize - afterSize;
        
        if (removedCount > 0) {
            logger.info("Token黑名单清理完成，共清理过期Token: {} 个，当前黑名单大小: {}", removedCount, afterSize);
        } else {
            logger.debug("Token黑名单清理完成，无过期Token需要清理，当前黑名单大小: {}", afterSize);
        }
    }

    /**
     * 每天凌晨2点执行一次全量清理（额外保障）
     * cron表达式: "0 0 2 * * ?" 表示每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyCleanup() {
        int beforeSize = tokenBlacklist.getBlacklistSize();
        
        tokenBlacklist.removeExpiredTokens();
        
        int afterSize = tokenBlacklist.getBlacklistSize();
        int removedCount = beforeSize - afterSize;
        
        logger.info("【每日Token黑名单清理】清理过期Token: {} 个，清理后黑名单大小: {}", removedCount, afterSize);
    }
}