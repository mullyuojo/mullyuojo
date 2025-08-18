package com.ojo.mullyuojo.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class DeliveryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


}
