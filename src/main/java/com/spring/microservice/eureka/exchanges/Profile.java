package com.spring.microservice.eureka.exchanges;

import java.util.Date;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class Profile implements java.io.Serializable {
    private Long id;
    private String name;
    private Date dob;
    private String occupation;

    public Profile() {}
}