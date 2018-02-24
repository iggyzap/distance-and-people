package com.izapolsky.distance.search;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.izapolsky.distance.search.api.Query;
import com.izapolsky.distance.search.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Main entry point of the tool
 */
public class Main {


    public static class CmdArgs {
        @Parameter(names={"--lat"}, description = "Latitude")
        public String latitude = "53.339428";

        @Parameter(names={"--lon"}, description = "Longitude")
        public String longitude = "-6.257664";


        @Parameter(names={"-d"}, description = "Distance in km for search")
        public float distanceKm = 100;

        @Parameter(names={"--help"}, description = "Displays help")
        public boolean help;
    }

    /**
     * Main entry point
     * @param args
     * @throws IOException
     */
    public static void main(String... args) throws IOException {
        CmdArgs cmdArgs = new CmdArgs();
        JCommander jc = new JCommander(cmdArgs);
        jc.setProgramName(Main.class.getName());
        jc.parse(args);
        if (cmdArgs.help) {
            jc.usage();
        } else {
            Main main = new Main(new H2GisStorageService(), new CustomerParserImpl(), new CustomerRendererImpl());
            try (InputStream is = wrapStdIn()) {
                main.process(is, new Query(cmdArgs.latitude, cmdArgs.longitude, cmdArgs.distanceKm), System.out);
            }
        }

    }

    private static InputStream wrapStdIn() {

        return new InputStream() {
            //we do not delegate close to make sure that in won't be closed
            final InputStream delegate = System.in;

            @Override
            public int read() throws IOException {
                return delegate.read();
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return delegate.read(b, off, len);
            }
        };
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

    /**
     * Runs processing in 2 stages - load of data, search for users satisfying query
     * @param data
     * @param query
     * @param where
     */
    public void process(InputStream data, Query query, PrintStream where) {
        Long processed = storageService.batchProcessCustomers(parser.parse(data), true);
        logger.info("Processed {} customer records", processed);
        customerRenderer.renderTo(storageService.findMatching(query), where);

    }

}
