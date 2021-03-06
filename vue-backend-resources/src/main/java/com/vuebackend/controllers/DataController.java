package com.vuebackend.controllers;

import java.util.Optional;

import com.vuebackend.communication.AddDatapointRequest;
import com.vuebackend.communication.FailureResponseMessage;
import com.vuebackend.communication.ResponseMessage;
import com.vuebackend.communication.SuccessResponseMessage;
import com.vuebackend.dbrepositories.DatapointRepository;
import com.vuebackend.dbrepositories.SensorRepository;
import com.vuebackend.dbrepositories.UserRepository;
import com.vuebackend.entities.Datapoint;
import com.vuebackend.entities.Endpoint;
import com.vuebackend.entities.Sensor;
import com.vuebackend.entitiydata.DatapointData;
import com.vuebackend.security.JwtTokenClaimUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/data")
@CrossOrigin(origins = "${allowedOrigins}")
public class DataController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatapointRepository datapointRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private RestTemplate restTemplate;
      
    @Value("${gatewayAddress}")
    private String gatewayAddress;


    @Bean
    private RestTemplate initRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addDatapoint(@RequestBody AddDatapointRequest request,
                                                        @RequestHeader("Authorization") String token) {
        
        String username = JwtTokenClaimUtils.getUsername(token);
        String endpointName = JwtTokenClaimUtils.getEndpointname(token);

        Optional<Endpoint> endpoint = 
                userRepository.findEndpointByNameOfUser(username, endpointName);

        Optional<Sensor> sensor =
                sensorRepository.findByName(username, endpointName, request.getSensorName());

        if(endpoint.isPresent() && sensor.isPresent()) {
            Datapoint datapoint = new Datapoint(sensor.get(),
                                                request.getDatapoint().getValue(),
                                                request.getDatapoint().getTime());

            this.datapointRepository.save(datapoint);
            
            this.restTemplate.put(this.gatewayAddress + "/datapoint",
                new DatapointData(request.getDatapoint().getValue(),
                                  request.getDatapoint().getTime(),
                                  sensor.get().getTopic()));
            
            return ResponseEntity.ok(new SuccessResponseMessage<Object>());
        }
        return ResponseEntity.ok(new FailureResponseMessage());
    }
}