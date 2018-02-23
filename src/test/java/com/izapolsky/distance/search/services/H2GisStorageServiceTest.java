package com.izapolsky.distance.search.services;

import com.izapolsky.distance.search.api.Customer;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class H2GisStorageServiceTest {

    private H2GisStorageService toTest;

    @Before
    public void setUp() {
        toTest = new H2GisStorageService();
    }

    @Test
    public void testCanProcessSingleCustomer() {
        assertEquals(new Long(1L),
                toTest.batchProcessCustomers(
                        Collections.singletonList(getCustomer("X y", 100L, "-6.043701", "52.986375")), true));
    }


    public Customer getCustomer(String name, Long userId, String longitude, String latitude) {
        Customer customer = new Customer();
        customer.name = name;
        customer.userId = userId;
        customer.longitude = longitude;
        customer.latitude = latitude;

        return customer;
    }
}