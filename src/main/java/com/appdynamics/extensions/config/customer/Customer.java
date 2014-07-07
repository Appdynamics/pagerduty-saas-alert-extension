package com.appdynamics.extensions.config.customer;


public class Customer {

    private String userAccount;
    private String password;
    private String saasHost;
    private PagerDutyConfig pagerDutyConfig;

    public PagerDutyConfig getPagerDutyConfig() {
        return pagerDutyConfig;
    }

    public void setPagerDutyConfig(PagerDutyConfig pagerDutyConfig) {
        this.pagerDutyConfig = pagerDutyConfig;
    }

    public String getSaasHost() {
        return saasHost;
    }

    public void setSaasHost(String saasHost) {
        this.saasHost = saasHost;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
}
