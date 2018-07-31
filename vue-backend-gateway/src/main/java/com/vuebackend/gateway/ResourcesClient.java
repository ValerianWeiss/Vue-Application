package com.vuebackend.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import com.vuebackend.communication.AddDatapointRequest;
import com.vuebackend.communication.AddEndpointRequest;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("vue-backend-resources")
public interface ResourcesClient {
    @RequestMapping(value = "/user/{username}/endpoint/all", method = RequestMethod.GET)
    Object getEndpoints(@PathVariable("username") String username);

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    Object addDatapoint(@RequestBody AddDatapointRequest request);

    @RequestMapping(value = "/endpoint", method = RequestMethod.POST)
    Object addEndpoint(@RequestBody AddEndpointRequest request,
                       @RequestHeader("Authorization") String headerValue);

    @RequestMapping(value = "/sensor", method = RequestMethod.POST)
    Object addSensor(@RequestBody AddEndpointRequest request);
}