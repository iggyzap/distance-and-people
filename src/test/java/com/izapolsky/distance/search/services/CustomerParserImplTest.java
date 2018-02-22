package com.izapolsky.distance.search.services;

import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.*;

public class CustomerParserImplTest {


    private CustomerParserImpl toTest = new CustomerParserImpl();

    private List<Closeable> toClose = new ArrayList<>();

    @After
    public void tearDown() {
        for (Closeable c : toClose) {
            IOUtils.closeQuietly(c );
        }
    }

    private <T extends Closeable> T markForClosing(T what) {
        toClose.add(what);

        return what;
    }

    @Test
    public void testReadsCustomers() throws Exception {
        assertEquals("Number of customers expected", 32,
                Iterables.size(toTest.parse(markForClosing(getClass().getResourceAsStream("/customers.json")))));

    }

    @Test(expected = RuntimeException.class)
    public void testReadFromBrokenStream() throws Exception {
        Iterables.size(toTest.parse(markForClosing(new GZIPInputStream(getClass().getResourceAsStream("/damaged_customers.json.gz")))));
    }

    @Test(expected = RuntimeException.class)
    public void testReadWithIOException() throws Exception {
        Iterables.size(toTest.parse(new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Ouch!");
            }
        }));
    }

    @Test(expected = RuntimeException.class)
    public void testReadBadData() throws Exception {
        Iterables.size(toTest.parse(markForClosing(getClass().getResourceAsStream("/bad_data.json"))));
    }

    @Test
    public void testNullStreamLazy() throws Exception {
        assertNotNull("Should create iterable lambda, but not touch null", toTest.parse(null));
    }

    @Test
    public void testNullStreamAccessNoData() throws Exception {
        assertEquals(0, Iterables.size(toTest.parse(null)));
    }

    @Test
    public void testDoesNotCloseStream() throws Exception {
        InputStream s = markForClosing(getClass().getResourceAsStream("/customers.json"));
        Iterables.size(toTest.parse(s));
        //statement below should not cause exception
        s.close();

    }

}