package com.appdynamics.extensions.config;


public class Configuration {

    private String protocol;
    private String applicationsUrlPath;
    private String healthRuleViolationsUrlPath;
    private String eventsUrlPath;
    private int connectTimeout;
    private int socketTimeout;
    private int scheduleInterval;
    private int numberOfThreads;
    private int threadTimeout;


    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getApplicationsUrlPath() {
        return applicationsUrlPath;
    }

    public void setApplicationsUrlPath(String applicationsUrlPath) {
        this.applicationsUrlPath = applicationsUrlPath;
    }

    public String getHealthRuleViolationsUrlPath() {
        return healthRuleViolationsUrlPath;
    }

    public void setHealthRuleViolationsUrlPath(String healthRuleViolationsUrlPath) {
        this.healthRuleViolationsUrlPath = healthRuleViolationsUrlPath;
    }

    public String getEventsUrlPath() {
        return eventsUrlPath;
    }

    public void setEventsUrlPath(String eventsUrlPath) {
        this.eventsUrlPath = eventsUrlPath;
    }


    public int getScheduleInterval() {
        return scheduleInterval;
    }

    public void setScheduleInterval(int scheduleInterval) {
        this.scheduleInterval = scheduleInterval;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public int getThreadTimeout() {
        return threadTimeout;
    }

    public void setThreadTimeout(int threadTimeout) {
        this.threadTimeout = threadTimeout;
    }
}
