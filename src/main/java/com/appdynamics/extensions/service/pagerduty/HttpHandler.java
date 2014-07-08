package com.appdynamics.extensions.service.pagerduty;

import com.appdynamics.extensions.config.Configuration;
import com.appdynamics.extensions.http.Response;
import com.appdynamics.extensions.http.SimpleHttpClient;
import org.apache.log4j.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class HttpHandler {

    public static final String HTTPS = "https";
    public static final String HTTP = "http";
    public static final String COLON = ":";
    public static final String FORWARD_SLASH = "/";

    private Configuration config;
    private static Logger logger = Logger.getLogger(HttpHandler.class);

    public HttpHandler(Configuration config){
        this.config = config;
    }

    /**
     * Posts the data on Pagerduty Endpoint.
     * @param data
     * @return
     */
    public Response postAlert(String data) {
        Map<String, String> httpConfigMap = createHttpConfigMap();
        logger.debug("Building the httpClient for pagerduty post");
        SimpleHttpClient simpleHttpClient = SimpleHttpClient.builder(httpConfigMap)
                .connectionTimeout(config.getPagerDutyConfig().getConnectTimeout() * 1000)
                .socketTimeout(config.getPagerDutyConfig().getSocketTimeout() * 1000)
                .build();
        String targetUrl = buildTargetUrl();
        logger.debug("Posting data to VO at " + targetUrl);
        Response response = simpleHttpClient.target(targetUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .post(data);
        logger.debug("HTTP Response status from VO " + response.getStatus());
        return response;
    }


    private Map<String, String> createHttpConfigMap() {
        Map<String,String> map = new HashMap<String, String>();
        if(isSSLEnabled()) {
            map.put("use-ssl", "true");
        }
        return map;
    }

    private boolean isSSLEnabled() {
        return config.getPagerDutyConfig().getProtocol().equalsIgnoreCase(HTTPS);
    }



    private String buildTargetUrl() {
        StringBuilder sb = new StringBuilder();
        if(isSSLEnabled()){
            sb.append(HTTPS);
        }
        else{
            sb.append(HTTP);
        }
        sb.append(COLON).append(FORWARD_SLASH)
                .append(FORWARD_SLASH)
                .append(config.getPagerDutyConfig().getHost())
                .append(config.getPagerDutyConfig().getUrlPath());

        return sb.toString();
    }
}
