pagerduty-saas-alert-extension
==========================

An AppDynamics extension for SAAS customers only to be used with a stand alone Java machine agent to integrate AppDynamic's controller events
to PagerDuty. 
###This extension is for SAAS customers only. For on-prem pager duty extension visit [here](http://community.appdynamics.com/t5/AppDynamics-eXchange/PagerDuty-Alerting-Extension/idi-p/747) ###.


## Use Case ##

PagerDuty provides SaaS IT on-call schedule management, alerting and incident tracking. AppDynamics integrates directly with PagerDuty to create incidents in response to alerts.
With the PagerDuty integration you can leverage your existing alerting infrastructure to notify the operations team to resolve performance degradation.

## Prerequisites ##

- You should have a PagerDuty Service Key


## Installation ##

1. Run "mvn clean install" and find the PagerdutySaasAlertExtension.zip file in the "target" folder. You can also download the PagerdutySaasAlertExtension.zip from [AppDynamics Exchange][].
2. Unzip as "PagerdutySaasAlertExtension" and copy the "PagerdutySaasAlertExtension" directory to `<MACHINE_AGENT_HOME>/monitors`



## Configuration ##

Note : Please make sure to not use tab (\t) while editing yaml files. You may want to validate the yaml file using a [yaml validator](http://yamllint.com/)

1. Configure the AppDynamics SaaS settings namely userAccount,password,saasHost and PagerDuty's serviceKey in the config.yaml file in `<MACHINE_AGENT_HOME>/monitors/PagerdutySaasAlertExtension/`.

   For eg.

   ```
        #AppDynamics user account information for the SAAS customer. Format is <appdynamics-username>@<appdynamics-account-name>
        userAccount: ""

        #AppDynamics password for SAAS customer
        password: ""

        #AppDynamics host for SAAS customer
        saasHost: ""

        #scheme used for REST AppD call (http/https)
        protocol: "https"

        applicationsUrlPath: "/controller/rest/applications"

        healthRuleViolationsUrlPath: "/controller/rest/applications/<#APP_ID#>/problems/healthrule-violations?time-range-type=BEFORE_NOW&duration-in-mins=<#POLLING_FREQ_IN_MINS#>&output=XML"

        eventsUrlPath: "/controller/rest/applications/<#APP_ID#>/events?time-range-type=BEFORE_NOW&duration-in-mins=<#POLLING_FREQ_IN_MINS#>&event-types=APPLICATION_ERROR,DIAGNOSTIC_SESSION&severities=ERROR&output=XML"

        # Interval in mins to pull the events from REST api.
        durationInMins: 1

        #http timeouts in sec
        connectTimeout: 10
        socketTimeout: 10


        #PagerDuty config for the customer
        pagerDutyConfig:

           #PagerDuty Service Key
           serviceKey: ""

           #scheme used (http/https)
           protocol: "https"

           #PagerDuty url path
           host: "events.pagerduty.com"

           #PagerDuty url path
           urlPath: "/generic/2010-04-15/create_event.json"

           #http timeouts in sec
           connectTimeout: 10
           socketTimeout: 10

           #show appdynamics details in pagerduty alert
           showDetails: true

   ```


3. Configure the path to the config.yaml file by editing the <task-arguments> in the monitor.xml file in the `<MACHINE_AGENT_HOME>/monitors/PagerdutySaasAlertExtension/` directory. Below is the sample

     ```
     <task-arguments>
         <!-- config file-->
         <argument name="config-file" is-required="true" default-value="monitors/PagerdutySaasAlertExtension/config.yaml" />
          ....
     </task-arguments>
    ```


## Contributing ##

Always feel free to fork and contribute any changes directly via [GitHub][].

## Community ##

Find out more in the [AppDynamics Exchange][].

## Support ##

For any questions or feature request, please contact [AppDynamics Center of Excellence][].

**Version:** 1.0.0
**Controller Compatibility:** 3.7+

[Github]: https://github.com/Appdynamics/pagerduty-saas-alert-extension
[AppDynamics Exchange]: http://community.appdynamics.com/t5/AppDynamics-eXchange/idb-p/extensions
[AppDynamics Center of Excellence]: mailto:ace-request@appdynamics.com
