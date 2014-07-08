package com.appdynamics.extensions;


import com.appdynamics.extensions.config.Configuration;

public class EndpointBuilder {

    public static final String HTTPS = "https://";
    public static final String APP_ID_HOLDER = "<#APP_ID#>";
    public static final String POLLING_FREQ_IN_MINS_HOLDER = "<#POLLING_FREQ_IN_MINS#>";

    public String buildApplicationsEndpoint(Configuration baseConfig) {
        StringBuffer sb = getHost(baseConfig).append(baseConfig.getApplicationsUrlPath());
        return sb.toString();
    }

    public String buildHealthRulesViolationEndpoint(Configuration baseConfig,int applicationId) {
        StringBuffer sb = getHost(baseConfig);
        sb.append(baseConfig.getHealthRuleViolationsUrlPath());
        return replacePlaceHolders(sb.toString(),applicationId,baseConfig.getDurationInMins());
    }


    public String buildEventsEndpoint(Configuration baseConfig,int applicationId) {
        StringBuffer sb = getHost(baseConfig);
        sb.append(baseConfig.getEventsUrlPath());
        return replacePlaceHolders(sb.toString(),applicationId,baseConfig.getDurationInMins());
    }

    private StringBuffer getHost(Configuration baseConfig){
        StringBuffer sb = new StringBuffer();
        sb.append(HTTPS).append(baseConfig.getSaasHost());
        return sb;
    }

    private String replacePlaceHolders(String str, int applicationId, int delaySchedule) {
        return str.replace(APP_ID_HOLDER,Integer.toString(applicationId)).replace(POLLING_FREQ_IN_MINS_HOLDER,Integer.toString(delaySchedule));
    }

}
