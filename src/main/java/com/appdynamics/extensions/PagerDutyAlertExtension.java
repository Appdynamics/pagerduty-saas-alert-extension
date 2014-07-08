package com.appdynamics.extensions;


import com.appdynamics.extensions.config.ConfigUtil;
import com.appdynamics.extensions.config.Configuration;
import com.appdynamics.extensions.service.appd.ServiceImpl;
import com.google.common.base.Strings;
import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PagerDutyAlertExtension extends AManagedMonitor {

    private static Logger logger = Logger.getLogger(PagerDutyAlertExtension.class);
    private ExecutorService threadPool;

    public static final String CONFIG_ARG = "config-file";

    //To load the config files
    final static ConfigUtil<Configuration> configUtil = new ConfigUtil<Configuration>();

    public PagerDutyAlertExtension() {
        String msg = "Using Monitor Version [" + getImplementationVersion() + "]";
        logger.info(msg);
        System.out.println(msg);
    }

    @Override
    public TaskOutput execute(Map<String, String> taskArgs, TaskExecutionContext taskExecutionContext) throws TaskExecutionException {
        if (taskArgs != null) {
            logger.info("Starting the PagerDuty SAAS Alerting extension");
            if (logger.isDebugEnabled()) {
                logger.debug("Task Arguments Passed ::" + taskArgs);
            }
            String configFilename = getConfigFilename(taskArgs.get(CONFIG_ARG));
            try {
                //load the base configuration
                Configuration config = configUtil.readConfig(configFilename, Configuration.class);
                CustomerTask customerTask = new CustomerTask(config,new ServiceImpl());
                customerTask.execute();
                return new TaskOutput("PagerDuty SAAS Alerting extension completed successfully.");
            } catch (FileNotFoundException e) {
                logger.error("Config file not found." + e);
            } catch (Exception e) {
                logger.error("Scheduled task failed." + e);
            }
        }
        throw new TaskExecutionException("PagerDuty SAAS Alerting extension completed with failures.");
    }


    /**
     * Returns a config file name,
     * @param filename
     * @return String
     */
    private String getConfigFilename(String filename) {
        if(filename == null){
            return "";
        }
        //for absolute paths
        if(new File(filename).exists()){
            return filename;
        }
        //for relative paths
        File jarPath = PathResolver.resolveDirectory(AManagedMonitor.class);
        String configFileName = "";
        if(!Strings.isNullOrEmpty(filename)){
            configFileName = jarPath + File.separator + filename;
        }
        return configFileName;
    }


    public static String getImplementationVersion() {
        return PagerDutyAlertExtension.class.getPackage().getImplementationTitle();
    }


   /* public static void main(String[] args){
        logger.info("Starting the saas-pager-duty-alerting extension");
        System.out.println("Starting PagerDuty Extension at " + System.currentTimeMillis());
        try {
            //load the base configuration
            Configuration config = configUtil.readConfig(CONFIG_FILENAME, Configuration.class);
            logger.debug("Successfully loaded the base config");
            //create a scheduled thread executor with frequency
            ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
            logger.debug("Scheduling the task processor every " + config.getDurationInMins());
           // scheduledThreadPool.scheduleAtFixedRate(new ScheduledTask(config), 0, config.getDurationInMins(), TimeUnit.SECONDS);
        } catch (FileNotFoundException e) {
            logger.error("Config file not found." + e);
        } catch (Exception e) {
            logger.error("Scheduled task failed." + e);
        }

        logger.info("Terminating the saas-pager-duty-alerting extension");
    }*/
}
