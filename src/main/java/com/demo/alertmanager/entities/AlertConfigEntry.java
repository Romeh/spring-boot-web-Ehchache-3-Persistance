package com.demo.alertmanager.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by id961900 on 08/08/2017.
 */
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class AlertConfigEntry implements Serializable {
    String serviceCode;
    String errorCode;
    List<String> emails;
    int maxCount;

}
