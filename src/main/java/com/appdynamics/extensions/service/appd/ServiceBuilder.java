/*
 * Copyright 2018. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.service.appd;


import com.appdynamics.TaskInputArgs;
import com.appdynamics.extensions.http.SimpleHttpClient;

import java.util.HashMap;
import java.util.Map;

public class ServiceBuilder {
    private boolean isSSLEnabled;
    private String userAccount;
    private String password;
    private int connectTimeout;
    private int socketTimeout;

    public ServiceBuilder(boolean isSSLEnabled,String userAccount,String password,int connectTimeout,int socketTimeout){
        this.isSSLEnabled = isSSLEnabled;
        this.userAccount = userAccount;
        this.password = password;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
    }

    public SimpleHttpClient buildHttpClient(Class clazz){
        Map<String, String> httpConfigMap = createHttpConfigMap();
        SimpleHttpClient simpleHttpClient = SimpleHttpClient.builder(httpConfigMap)
                .connectionTimeout(connectTimeout)
                .socketTimeout(socketTimeout)
                .jaxbClasses(clazz)
                .build();
        return simpleHttpClient;
    }

    private Map<String, String> createHttpConfigMap() {
        Map<String,String> map = new HashMap<String, String>();
        map.put(TaskInputArgs.USER,userAccount);
        map.put(TaskInputArgs.PASSWORD,password);
        if(isSSLEnabled) {
            map.put(TaskInputArgs.USE_SSL, "true");
        }
        return map;
    }

    public boolean isSSLEnabled() {
        return isSSLEnabled;
    }
}
