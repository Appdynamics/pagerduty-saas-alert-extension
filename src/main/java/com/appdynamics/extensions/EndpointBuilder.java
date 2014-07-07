package com.appdynamics.extensions;


import com.appdynamics.extensions.config.Configuration;
import com.appdynamics.extensions.config.customer.Customer;

public class EndpointBuilder {

    public static final String HTTPS = "https://";
    public static final String APP_ID_HOLDER = "<#APP_ID#>";
    public static final String POLLING_FREQ_IN_MINS_HOLDER = "<#POLLING_FREQ_IN_MINS#>";

    public String buildApplicationsEndpoint(Configuration baseConfig, Customer customer) {
        StringBuffer sb = getHost(customer).append(baseConfig.getApplicationsUrlPath());
        return sb.toString();
    }

    public String buildHealthRulesViolationEndpoint(Configuration baseConfig, Customer customer, int applicationId) {
        StringBuffer sb = getHost(customer);
        sb.append(baseConfig.getHealthRuleViolationsUrlPath());
        return replacePlaceHolders(sb.toString(),applicationId,baseConfig.getScheduleInterval());
    }


    public String buildEventsEndpoint(Configuration baseConfig, Customer customer,int applicationId) {
        StringBuffer sb = getHost(customer);
        sb.append(baseConfig.getEventsUrlPath());
        return replacePlaceHolders(sb.toString(),applicationId,baseConfig.getScheduleInterval());
    }

    private StringBuffer getHost(Customer customer){
        StringBuffer sb = new StringBuffer();
        sb.append(HTTPS).append(customer.getSaasHost());
        return sb;
    }

    private String replacePlaceHolders(String str, int applicationId, int delaySchedule) {
        return str.replace(APP_ID_HOLDER,Integer.toString(applicationId)).replace(POLLING_FREQ_IN_MINS_HOLDER,Integer.toString(delaySchedule / 60));
    }

}
