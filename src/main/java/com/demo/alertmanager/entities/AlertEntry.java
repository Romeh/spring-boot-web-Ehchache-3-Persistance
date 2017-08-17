package com.demo.alertmanager.entities;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by id961900 on 19/07/2017.
 */
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AlertEntry implements Serializable {
    private List<String> errors;
    private String errorCode;
    private String serviceId;
    private String severity;
}
