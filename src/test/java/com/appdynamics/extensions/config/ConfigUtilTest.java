package com.appdynamics.extensions.config;


import com.appdynamics.extensions.config.customer.CustomerConfig;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class ConfigUtilTest {



    @Test
    public void canLoadBaseConfigFile() throws FileNotFoundException {
        ConfigUtil<Configuration> configUtil = new ConfigUtil<Configuration>();
        Configuration configuration = configUtil.readConfig(this.getClass().getResource("/conf/config.yaml").getFile(),Configuration.class);
        Assert.assertTrue(configuration != null);
    }

    @Test
    public void canLoadCustomerConfigFile() throws FileNotFoundException {
        ConfigUtil<CustomerConfig> configUtil = new ConfigUtil<CustomerConfig>();
        CustomerConfig customerConfig = configUtil.readConfig(this.getClass().getResource("/conf/customers.yaml").getFile(),CustomerConfig.class);
        Assert.assertTrue(customerConfig != null);
    }

}
