package com.demo.alertmanager.repositories;

import com.demo.alertmanager.entities.AlertConfigEntry;
import com.demo.alertmanager.entities.AlertEntry;
import com.demo.alertmanager.entities.AlertHolder;

import java.util.List;

/**
 * Created by id961900 on 08/08/2017.
 */
public interface AlertsStore {
    List<AlertEntry> getAlertForServiceId(String serviceId);

    void updateAlertEntry(String serviceId, String errorCode, AlertEntry alertEntry);

    List<AlertHolder> getAllAlerts();

    void deleteAlertEntry(String serviceId, String errorCodeCode);

    void createAlertEntry(AlertEntry alertEntry);

    AlertConfigEntry getConfigForServiceIdCodeId(String serviceId, String codeId);

    void update(String serviceId, String codeId, AlertConfigEntry alertConfigEntry);
}
