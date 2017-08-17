package com.demo.alertmanager;

import com.demo.alertmanager.entities.AlertsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PreDestroy;
import javax.cache.CacheManager;


@SpringBootApplication
@EnableCaching
public class AlertManagerApplication {

    public static void main(String[] args) {

        final ConfigurableApplicationContext run = SpringApplication.run(AlertManagerApplication.class, args);
        AlertsConfiguration cfg = run.getBean(AlertsConfiguration.class);
        cfg.getAlertConfigurations().forEach(alertConfigEntity -> System.out.println(alertConfigEntity.getEmails()));

    }
    

}
