package com.appdynamics.extensions;

import com.appdynamics.extensions.config.ConfigUtil;
import com.appdynamics.extensions.config.Configuration;
import com.appdynamics.extensions.config.customer.Customer;
import com.appdynamics.extensions.config.customer.CustomerConfig;
import com.appdynamics.extensions.service.appd.IService;
import com.appdynamics.extensions.service.appd.ServiceException;
import com.appdynamics.extensions.service.appd.ServiceImpl;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ScheduledTask implements Runnable{

    private ExecutorService threadPool;
    private Configuration baseConfig;

    //To load the config files
    private final static ConfigUtil<CustomerConfig> configUtil = new ConfigUtil<CustomerConfig>();

    private static final int DEFAULT_NUMBER_OF_THREADS = 10;
    public static final int DEFAULT_THREAD_TIMEOUT = 10;
    public static final String CONFIG_FILENAME =  "." + File.separator + "conf" + File.separator + "customers.yaml";

    private static Logger logger = Logger.getLogger(ScheduledTask.class);

    public ScheduledTask(Configuration baseConfig){
        this.baseConfig = baseConfig;
        threadPool = Executors.newFixedThreadPool(baseConfig.getNumberOfThreads() == 0 ? DEFAULT_NUMBER_OF_THREADS : baseConfig.getNumberOfThreads());
    }

    @Override
    public void run() {
        System.out.println("Running Scheduled task at " + System.currentTimeMillis());
        CustomerConfig config = null;
        try {
            //load the customer configuration
            config = configUtil.readConfig(CONFIG_FILENAME, CustomerConfig.class);
            logger.debug("Successfully loaded the customer config");
            //create concurrent tasks for customers
            List<Future<Void>> concurrentTasks = createConcurrentTasks(config);
            executeTasks(concurrentTasks,baseConfig.getThreadTimeout() == 0 ? DEFAULT_THREAD_TIMEOUT : baseConfig.getThreadTimeout());

        } catch (FileNotFoundException e) {
            logger.error("Customer Config file not found." + e);
        }




    }

    private List<Future<Void>> createConcurrentTasks(CustomerConfig customerConfig) {
        List<Future<Void>> concurrentTasks = new ArrayList<Future<Void>>();
        if (customerConfig != null && customerConfig.getCustomers() != null) {
            for (Customer customer : customerConfig.getCustomers()) {
                IService service = new ServiceImpl(); // TODO not necessary to create new obj every time
                CustomerTask customerTask = new CustomerTask(baseConfig,customer,service);
                concurrentTasks.add(threadPool.submit(customerTask));
            }
        }
        return concurrentTasks;
    }


    private void executeTasks(List<Future<Void>> concurrentTasks,int timeout) {
        for (Future<Void> aTask : concurrentTasks) {
            try {
                aTask.get(timeout, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("Task interrupted." + e);
            } catch (ExecutionException e) {
                logger.error("Task execution failed." + e);
            } catch (TimeoutException e) {
                logger.error("Task timed out." + e);
            }

        }
    }
}
