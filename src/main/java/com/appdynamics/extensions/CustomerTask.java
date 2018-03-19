/*
 * Copyright 2018. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions;


import com.appdynamics.extensions.alerts.customevents.EvaluationEntity;
import com.appdynamics.extensions.alerts.customevents.Event;
import com.appdynamics.extensions.alerts.customevents.HealthRuleViolationEvent;
import com.appdynamics.extensions.alerts.customevents.OtherEvent;
import com.appdynamics.extensions.config.Configuration;
import com.appdynamics.extensions.http.Response;
import com.appdynamics.extensions.service.appd.IService;
import com.appdynamics.extensions.service.appd.ServiceBuilder;
import com.appdynamics.extensions.service.appd.ServiceException;
import com.appdynamics.extensions.service.appd.app.Application;
import com.appdynamics.extensions.service.appd.hrv.PolicyViolation;
import com.appdynamics.extensions.service.pagerduty.Alert;
import com.appdynamics.extensions.service.pagerduty.AlertBuilder;
import com.appdynamics.extensions.service.pagerduty.HttpHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerTask {

    private Configuration baseConfig;
    private IService service;
    private EndpointBuilder endpointBuilder = new EndpointBuilder();
    private AlertBuilder alertBuilder = new AlertBuilder();

    private static Logger logger = Logger.getLogger(CustomerTask.class);

    public CustomerTask(Configuration baseConfig, IService service){
        this.baseConfig = baseConfig;
        this.service = service;
    }

    public void execute() throws Exception {
        ServiceBuilder builder = new ServiceBuilder(true,baseConfig.getUserAccount(),baseConfig.getPassword(),baseConfig.getConnectTimeout() * 1000,baseConfig.getSocketTimeout() * 1000);
        try {
            //get all applications for customer
            String appEndpoint = endpointBuilder.buildApplicationsEndpoint(baseConfig);
            logger.debug("Get Applications Call :" + appEndpoint);
            List<Application> applications = service.getApplications(builder, appEndpoint);
            List<Event> allEvents = new ArrayList<Event>();
            addHealthRuleViolationEvents(builder, applications, allEvents);
            //addOtherEvents(builder,applications,allEvents);
            //send to pagerduty
            sendToPagerDuty(allEvents);
        }
        catch (ServiceException e){
            logger.error("Service Exception." + e);
            throw e;
        }
    }

    private void sendToPagerDuty(List<Event> allEvents) {
        for(Event event : allEvents){
            Alert alert = null;
            if(event instanceof HealthRuleViolationEvent) {
                HealthRuleViolationEvent violationEvent = (HealthRuleViolationEvent) event;
                alert = alertBuilder.buildAlertFromHealthRuleViolationEvent(violationEvent, baseConfig.getPagerDutyConfig().getServiceKey());
            }
            else{
                OtherEvent otherEvent = (OtherEvent) event;
                alert = alertBuilder.buildAlertFromOtherEvent(otherEvent,baseConfig.getPagerDutyConfig().getServiceKey());
            }
            if (alert != null) {
                try {
                    HttpHandler handler = new HttpHandler(baseConfig);
                    String json = alertBuilder.convertIntoJsonString(alert);
                    logger.debug("Json posted to VO ::" + json);
                    Response response = handler.postAlert(json);
                    if (response != null && response.getStatus() == HttpURLConnection.HTTP_OK) {
                        logger.info("Data successfully posted to PagerDuty");
                        return;
                    }
                    logger.error("Data post failed");
                } catch (JsonProcessingException e) {
                    logger.error("Cannot serialized object into Json." + e);
                }
            }
        }
    }


    private void addHealthRuleViolationEvents(ServiceBuilder builder, List<Application> applications, List<Event> allEvents) {
        for(Application app : applications){
            String hrvEndpoint = endpointBuilder.buildHealthRulesViolationEndpoint(baseConfig, app.getId());
            logger.debug("Get Health Rule Violations Endpoint: " + hrvEndpoint);
            List<PolicyViolation> healthViolations = service.getHealthRuleViolations(builder, hrvEndpoint);
            List<HealthRuleViolationEvent> hrvEvents = translate(app,healthViolations);
            allEvents.addAll(hrvEvents);
        }
    }

    private List<HealthRuleViolationEvent> translate(Application app,List<PolicyViolation> healthViolations) {
        List<HealthRuleViolationEvent> healthRuleViolationEvents = new ArrayList<HealthRuleViolationEvent>();
        for(PolicyViolation healthViolation : healthViolations){
            HealthRuleViolationEvent hrvEvent = new HealthRuleViolationEvent();
            hrvEvent.setAppID(Integer.toString(app.getId()));
            hrvEvent.setAppName(app.getName());
            hrvEvent.setHealthRuleID(healthViolation.getId());
            hrvEvent.setHealthRuleName(healthViolation.getName());
            hrvEvent.setDeepLinkUrl(healthViolation.getDeepLinkUrl());
            hrvEvent.setIncidentID(healthViolation.getIncidentStatus());
            hrvEvent.setSummaryMessage(healthViolation.getDescription());
            hrvEvent.setPvnAlertTime(new Date(healthViolation.getStartTimeInMillis()).toString());
            hrvEvent.setSeverity(healthViolation.getSeverity());
            hrvEvent.setAffectedEntityID(healthViolation.getAffectedEntityDefinition().getEntityId());
            hrvEvent.setAffectedEntityName(healthViolation.getAffectedEntityDefinition().getEntityType());
            hrvEvent.setEventType(healthViolation.getIncidentStatus());
            hrvEvent.setPvnTimePeriodInMinutes(new Date(healthViolation.getDetectedTimeInMillis()).toString());
            EvaluationEntity ev = new EvaluationEntity();
            ev.setId(healthViolation.getTriggeredEntityDefinition().getEntityId());
            ev.setName(healthViolation.getTriggeredEntityDefinition().getEntityType());
            hrvEvent.setEvaluationEntity(Lists.newArrayList(ev));
            healthRuleViolationEvents.add(hrvEvent);
        }
        return healthRuleViolationEvents;
    }



     /* private void addOtherEvents(ServiceBuilder builder, List<Application> applications, List<Event> allEvents) {
        for(Application app : applications){
            String eventsEndpoint = endpointBuilder.buildEventsEndpoint(baseConfig, customer, app.getId());
            List<com.appdynamics.extensions.service.appd.events.Event> events = service.getEvents(builder, eventsEndpoint);
            List<OtherEvent> otherEvents = translate(events);
            allEvents.addAll(otherEvents);
        }
    }

    private List<OtherEvent> translate(List<com.appdynamics.extensions.service.appd.events.Event> events) {
        List<OtherEvent> otherEvents = new ArrayList<OtherEvent>();
        for(com.appdynamics.extensions.service.appd.events.Event event:events){
            OtherEvent otherEvent = new OtherEvent();
            otherEvent.setEventNotificationId(event.getId());
            EventType eventType = new EventType();
            eventType.setEventType(event.getType());
            otherEvent.setEventTypes(Lists.newArrayList(eventType));
        }
        return null;
    }*/
}
