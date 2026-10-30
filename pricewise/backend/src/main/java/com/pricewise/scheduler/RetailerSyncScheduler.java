package com.pricewise.scheduler;

import com.pricewise.service.RetailerSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RetailerSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(RetailerSyncScheduler.class);
    private final RetailerSyncService syncService;

    public RetailerSyncScheduler(RetailerSyncService syncService) {
        this.syncService = syncService;
    }

    @Scheduled(fixedRateString = "${pricewise.retailers.sync.scheduled-rate-ms:3600000}", initialDelay = 60000)
    public void runScheduledSync() {
        log.info("Running scheduled retailer price synchronization...");
        try {
            syncService.syncAllRetailers();
        } catch (Exception ex) {
            log.error("Scheduled retailer sync encountered error: {}", ex.getMessage());
        }
    }
}

