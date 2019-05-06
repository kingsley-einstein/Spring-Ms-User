package com.spring.microservice.eureka.controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;
import java.util.UUID;

//import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty.Type;
//import com.netflix.discovery.DiscoveryClient;
import com.spring.microservice.eureka.exceptions.UserNotFoundException;
import com.spring.microservice.eureka.exchanges.Profile;
import com.spring.microservice.eureka.model.User;
import com.spring.microservice.eureka.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
//import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] message = md.digest(user.getPassword().getBytes());
            BigInteger integer = new BigInteger(1, message);
            User newUser = new User(user.getUsername(), integer.toString(16));

            return repository.save(newUser);
        } catch(NoSuchAlgorithmException e) {
            System.err.println(e);

            return null;
        }
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) {
        return repository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @GetMapping
    public User getByUsername(@RequestParam("username") String username) {
        return repository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @GetMapping("/profile/{id}")
    public Profile getUserProfile(@PathVariable("id") Long id) {
        RestTemplate template = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("profile-service");
        String uri = String.format("%s/profile/%s",
        instances.get(0)
        .getUri()
        .toString(),
        id);
        ResponseEntity<Profile> response = template.exchange(uri, HttpMethod.GET, null, Profile.class);

        return response.getBody();
    }
}