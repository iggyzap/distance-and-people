package com.izapolsky.distance.search.services;

import com.izapolsky.distance.search.api.Customer;

import java.io.InputStream;

public interface CustomerParser {

    /**
     * Generates an iterable which start using stream on-demand.
     * It will not close associated stream
     * @param is stream to read from
     * @return lazy iterable to parse customers
     */
    public Iterable<Customer> parse(InputStream is);
}
