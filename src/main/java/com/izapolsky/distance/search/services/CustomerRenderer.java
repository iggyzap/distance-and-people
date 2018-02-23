package com.izapolsky.distance.search.services;

import com.izapolsky.distance.search.api.Customer;

import java.io.PrintStream;

/**
 * A renderer for customers
 */
public interface CustomerRenderer {

    /**
     * Renders data of given customers into specified destination
     * @param customers customers
     * @param to where to render
     */
    void renderTo(Iterable<Customer> customers, PrintStream to);
}
