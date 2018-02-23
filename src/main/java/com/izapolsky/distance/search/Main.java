package com.izapolsky.distance.search;

import com.izapolsky.distance.search.api.Query;
import com.izapolsky.distance.search.services.CustomerParser;
import com.izapolsky.distance.search.services.CustomerParserImpl;
import com.izapolsky.distance.search.services.CustomerRenderer;
import com.izapolsky.distance.search.services.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Main entry point of the tool
 */
public class Main {

    public static void main(String ... args) throws IOException {

        Main main = new Main(null, new CustomerParserImpl(), null);
        try (InputStream is = openStream(args)) {
            main.process(is, new Query(), System.out);
        }

    }

    private static InputStream openStream(String[] args) {
        return null;
    }
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final StorageService storageService;
    private final CustomerParser parser;
    private final CustomerRenderer customerRenderer;

    public Main(StorageService storageService, CustomerParser parser, CustomerRenderer customerRenderer) {
        this.storageService = storageService;
        this.parser = parser;
        this.customerRenderer = customerRenderer;
    }

    public void process(InputStream data, Query query, PrintStream where) {
        //2 stages :
        // optionally load data
        // render search
        Long processed = storageService.batchProcessCustomers(parser.parse(data), true);
        logger.info("Processed {} customer records", processed);
        customerRenderer.renderTo(storageService.findMatching(query), where);

    }

}
