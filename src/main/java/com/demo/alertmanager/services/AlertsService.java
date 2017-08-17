package com.demo.alertmanager.services;

import com.demo.alertmanager.entities.AlertConfigEntry;
import com.demo.alertmanager.entities.AlertEntry;
import com.demo.alertmanager.entities.AlertHolder;
import com.demo.alertmanager.repositories.AlertsStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by id961900 on 09/08/2017.
 */
@Service
public class AlertsService {
    private static final Logger logger = LoggerFactory.getLogger(AlertsService.class);

    @Autowired
    private AlertsStore alertsStore;


    public void createAlertEntry(AlertEntry alertEntry) {
        logger.debug("createAlertEntry service call with {}",alertEntry.toString());
        alertsStore.createAlertEntry(alertEntry);

    }

    public List<AlertEntry> getAlertForServiceId(String serviceId) {
        logger.debug("GetAlertForServiceId service call with {}",serviceId);
        return alertsStore.getAlertForServiceId(serviceId);
    }


    public void updateAlertEntry(String serviceId, String serviceCode, AlertEntry alertEntry) {
        logger.debug("updateAlertEntry service call with {}, {}, {}",serviceId,serviceCode,alertEntry.toString());
        alertsStore.updateAlertEntry(serviceId, serviceCode, alertEntry);
    }


    public List<AlertHolder> getAllAlerts() {
        logger.debug("getAllAlerts service call");
        return alertsStore.getAllAlerts();
    }


    public void deleteAlertEntry(String serviceId, String serviceCode) {
        logger.debug("deleteAlertEntry service call: {}, {}",serviceCode,serviceCode);
        alertsStore.deleteAlertEntry(serviceId, serviceCode);
    }


    public AlertConfigEntry getConfigForServiceIdCodeId(String serviceId, String codeId) {
        logger.debug("getConfigForServiceIdCodeId service call: {},{}",serviceId,codeId);
        return alertsStore.getConfigForServiceIdCodeId(serviceId, codeId);
    }


    public void updateAlertConfig(String serviceId, String codeId, AlertConfigEntry alertConfigEntry) {
        logger.debug("updateAlertConfig service call: {}, {}, {}",serviceId,codeId,alertConfigEntry.toString());
        alertsStore.update(serviceId, codeId, alertConfigEntry);
    }


}
