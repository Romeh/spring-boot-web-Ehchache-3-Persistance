package com.demo.alertmanager.repositories.impl;

import com.demo.alertmanager.entities.AlertConfigEntry;
import com.demo.alertmanager.entities.AlertEntry;
import com.demo.alertmanager.entities.AlertHolder;
import com.demo.alertmanager.entities.CacheNames;
import com.demo.alertmanager.exception.ResourceNotFoundException;
import com.demo.alertmanager.repositories.AlertsStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.cache.Cache;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class EhcacheAlertsSore implements AlertsStore {
    private static final Logger logger = LoggerFactory.getLogger(EhcacheAlertsSore.class);

    @Autowired
    private javax.cache.CacheManager cacheManager;



    @Override
    public List<AlertEntry> getAlertForServiceId(String serviceId) {
        return Optional.ofNullable(getAlertsCache().get(serviceId))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Alert for %s not found", serviceId)));

    }

    @Override
    public void updateAlertEntry(String serviceId, String serviceCode, AlertEntry alertEntry) {
        final Cache<String, List<AlertEntry>> alertsCache = getAlertsCache();
        alertsCache.invoke(serviceId, (mutableEntry, objects) -> {
            if (mutableEntry.exists() && mutableEntry.getValue() != null) {
                logger.debug("updating alert entry into the cache store invoke: {},{}",serviceId,serviceCode);
                final List<AlertEntry> alertEntries = mutableEntry.getValue();
                alertEntries.removeIf(alertEntry1 -> alertEntry1.getErrorCode().equals(serviceCode));
                alertEntries.add(alertEntry);
                mutableEntry.setValue(alertEntries);
            }else{
                throw new ResourceNotFoundException(String.format("Alert for %s with %s not found", serviceId,serviceCode));
            }
            // by api design nothing needed here
            return null;
        });
    }

    @Override
    public List<AlertHolder> getAllAlerts() {
        return StreamSupport.stream(getAlertsCache().spliterator(), false).map(stringListEntry -> AlertHolder.builder().
                alerts(stringListEntry.getValue()).
                serviceCode(stringListEntry.getKey()).build()).collect(Collectors.toList());

    }

    @Override
    public void deleteAlertEntry(String serviceId, String serviceCode) {
        final Cache<String, List<AlertEntry>> alertsCache = getAlertsCache();
        alertsCache.invoke(serviceId, (mutableEntry, objects) -> {
            if (mutableEntry.exists() && mutableEntry.getValue() != null) {
                logger.debug("deleting alert entry into the cache store invoke: {},{}",serviceId,serviceCode);
                final List<AlertEntry> alertEntries = mutableEntry.getValue();
                alertEntries.removeIf(alertEntry1 -> alertEntry1.getErrorCode().equals(serviceCode));
                mutableEntry.setValue(alertEntries);
            }else{
                throw new ResourceNotFoundException(String.format("Alert for %s with %s not found", serviceId,serviceCode));
            }
            // by api design nothing needed here
            return null;
        });

    }

    @Override
    public void createAlertEntry(AlertEntry alertEntry) {
        final Cache<String, List<AlertEntry>> alertsCache = getAlertsCache();
        final int maxCount = getConfigForServiceIdCodeIdCount(alertEntry.getServiceId(), alertEntry.getErrorCode());

        boolean isMaxCountExceeded = alertsCache.invoke(alertEntry.getServiceId(), (mutableEntry, objects) -> {
            if (mutableEntry.exists() && mutableEntry.getValue() != null) {
                logger.debug("creating alert entries into existing alerts list ",alertEntry.toString());
                final List<AlertEntry> alertEntries = mutableEntry.getValue();
                alertEntries.add(alertEntry);
                mutableEntry.setValue(alertEntries);
                if (alertEntries.size() >= maxCount) {
                    return true;
                }
                return false;
            } else {
                logger.debug("creating brand new alert entries into new alerts list ",alertEntry.toString());
                List<AlertEntry> firstTimeToAdd = new ArrayList<>();
                firstTimeToAdd.add(alertEntry);
                mutableEntry.setValue(firstTimeToAdd);
                return false;
            }
        });

        if (isMaxCountExceeded) {
            logger.debug("max alerts count is reached for : {}, start sending mail alert ",alertEntry.toString());
            // send the mail notification
        }

    }


    @Override
    public AlertConfigEntry getConfigForServiceIdCodeId(String serviceId, String codeId) {
        return Optional.ofNullable(getAlertsConfigCache().get(serviceId + "_" + codeId))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Alert config for %s with %s not found", serviceId,codeId)));
    }

    @Override
    public void update(String serviceId, String codeId, AlertConfigEntry alertConfigEntry) {
        getAlertsConfigCache().put(serviceId + "_" + codeId, alertConfigEntry);
    }

    public int getConfigForServiceIdCodeIdCount(String serviceId, String codeId) {
        final AlertConfigEntry alertConfigEntry = getAlertsConfigCache().get(serviceId + "_" + codeId);
        if(alertConfigEntry!=null){
            return alertConfigEntry.getMaxCount();
        }else{
            return 1;
        }

    }

    public Cache<String, List<AlertEntry>> getAlertsCache() {

        return cacheManager.getCache(CacheNames.Alerts.name());
    }

    public Cache<String, AlertConfigEntry> getAlertsConfigCache() {
        return cacheManager.getCache(CacheNames.AlertsConfig.name());
    }

    @PreDestroy
    public void close(){
        cacheManager.close();
    }
}
