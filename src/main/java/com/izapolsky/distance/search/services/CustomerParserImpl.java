package com.izapolsky.distance.search.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.izapolsky.distance.search.api.Customer;

import java.io.IOException;
import java.io.InputStream;

public class CustomerParserImpl implements CustomerParser {

    private ObjectMapper mapper = new ObjectMapper();
    private JsonFactory factory = new JsonFactory();

    @Override
    public Iterable<Customer> parse(InputStream is) {

        return () -> {
            try {
                return mapper.readValues(factory.createParser(is), Customer.class);
            } catch (IOException e) {
                //todo - may be revisit this at later stage. can we ignore errors and proceed to next record ?
                throw new RuntimeException(String.format("Failed reading from %1$s", is), e);
            }
        };
    }
}
