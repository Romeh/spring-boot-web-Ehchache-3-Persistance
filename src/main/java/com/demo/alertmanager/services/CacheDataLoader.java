package com.demo.alertmanager.services;

import com.demo.alertmanager.entities.AlertConfigEntry;
import com.demo.alertmanager.entities.AlertsConfiguration;
import com.demo.alertmanager.entities.CacheNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.cache.Cache;

/**
 * Created by id961900 on 11/08/2017.
 */
@Service
public class CacheDataLoader {


    @Autowired
    private javax.cache.CacheManager cacheManager;

    @Autowired
    private AlertsConfiguration alertsConfig;


    @PostConstruct
    public void init() {
        final Cache<String, AlertConfigEntry> alertsConfig = cacheManager.getCache(CacheNames.AlertsConfig.name());
        this.alertsConfig.getAlertConfigurations().forEach(alertConfigEntity -> {
            alertsConfig.put(alertConfigEntity.getServiceCode() + "_" + alertConfigEntity.getErrorCode(),
                    alertConfigEntity);

        });

    }


}
