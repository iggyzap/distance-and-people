package com.izapolsky.distance.search.services;

import com.google.common.collect.Iterables;
import com.izapolsky.distance.search.api.Customer;
import com.izapolsky.distance.search.api.Query;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
                        Collections.singletonList(getCustomer("X y", 100L, "52.986375", "-6.043701")), true));
    }

    @Test
    public void testCanLocateCustomer() {
        List<Customer> l = Arrays.asList(
                getCustomer("Milltown 1", 100L, "53.313507", "-6.247295"),
                getCustomer("UCD Belfield", 200L, "53.310543", "-6.227837")
        );
        assertEquals(new Long(l.size()), toTest.batchProcessCustomers(l , true));
        Iterable<Customer> it = toTest.findMatching(new Query("53.316378", "-6.239999", 0.7f));
        assertEquals(1, Iterables.size(it));
        assertEquals("Milltown 1", it.iterator().next().name);
    }

    @Test
    public void testDotsAndRange() {
        List<Customer> l = Arrays.asList(
                getCustomer("Roebuck In", 100L, "53.311935", "-6.232731"),
                getCustomer("Beech Hill Out", 200L, "53.3114", "-6.233471")
        );
        assertEquals(new Long(l.size()), toTest.batchProcessCustomers(l , true));
        Iterable<Customer> it = toTest.findMatching(new Query("53.316378", "-6.239999", 0.7f));
        assertEquals(1, Iterables.size(it));
        assertEquals("Roebuck In", it.iterator().next().name);
    }

    @Test
    public void testBadDataNoCounters() {
        assertEquals(new Long(0L),
                toTest.batchProcessCustomers(
                        Arrays.asList(
                                getCustomer(null, 100L, "52.986375", "-6.043701"),
                                getCustomer("x", null, "52.986375", "-6.043701"),
                                getCustomer("x", 100l, "52.986375", null),
                                getCustomer("x", 100l, null, "-6.043701")), true));
    }


    public static Customer getCustomer(String name, Long userId, String latitude, String longitude) {
        Customer customer = new Customer();
        customer.name = name;
        customer.userId = userId;
        customer.longitude = longitude;
        customer.latitude = latitude;

        return customer;
    }
}