package com.appdynamics.extensions;


import com.appdynamics.extensions.config.ConfigUtil;
import com.appdynamics.extensions.config.Configuration;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PagerDutyAlertExtension {

    private static Logger logger = Logger.getLogger(PagerDutyAlertExtension.class);
    public static final String CONFIG_FILENAME =  "." + File.separator + "conf" + File.separator + "config.yaml";
    //To load the config files
    final static ConfigUtil<Configuration> configUtil = new ConfigUtil<Configuration>();


    public static void main(String[] args){
        logger.info("Starting the saas-pager-duty-alerting extension");
        System.out.println("Starting PagerDuty Extension at " + System.currentTimeMillis());
        try {
            //load the base configuration
            Configuration config = configUtil.readConfig(CONFIG_FILENAME, Configuration.class);
            logger.debug("Successfully loaded the base config");
            //create a scheduled thread executor with frequency
            ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
            logger.debug("Scheduling the task processor every " + config.getScheduleInterval());
            scheduledThreadPool.scheduleAtFixedRate(new ScheduledTask(config), 0, config.getScheduleInterval(), TimeUnit.SECONDS);
        } catch (FileNotFoundException e) {
            logger.error("Config file not found." + e);
        } catch (Exception e) {
            logger.error("Scheduled task failed." + e);
        }

        logger.info("Terminating the saas-pager-duty-alerting extension");
    }




    // from scheduled thread executor spawn multiple threads
    // task contains customer with pagerduty info
    // makes a call to REST apis, collects information and triggers pager duty
}
