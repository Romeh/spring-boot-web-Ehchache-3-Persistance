package com.demo.alertmanager.services;

/**
 * Created by id961900 on 08/08/2017.
 */

import com.demo.alertmanager.entities.AlertConfigEntry;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEventLogger implements CacheEventListener<String, AlertConfigEntry> {

    private static final Logger logger = LoggerFactory.getLogger(CacheEventLogger.class);

    @Override
    public void onEvent(CacheEvent<? extends String, ? extends AlertConfigEntry> cacheEvent) {
        logger.debug("Event: " + cacheEvent.getType() + " Key: " + cacheEvent.getKey() + " old value: " + cacheEvent.getOldValue() + " new value: " + cacheEvent.getNewValue());
    }
}
