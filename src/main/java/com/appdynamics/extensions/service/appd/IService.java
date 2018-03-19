/*
 * Copyright 2018. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.service.appd;

import com.appdynamics.extensions.service.appd.app.Application;
import com.appdynamics.extensions.service.appd.events.Event;
import com.appdynamics.extensions.service.appd.hrv.PolicyViolation;

import java.util.List;

/**
 * Interface for AppD Controller Rest APIs
 */
public interface IService {

    List<Application> getApplications(ServiceBuilder serviceBuilder,String endpoint) throws ServiceException;

    List<PolicyViolation> getHealthRuleViolations(ServiceBuilder serviceBuilder,String endpoint) throws ServiceException;

    List<Event>  getEvents(ServiceBuilder serviceBuilder,String endpoint) throws ServiceException;
}
