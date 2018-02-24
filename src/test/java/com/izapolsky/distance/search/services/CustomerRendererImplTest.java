package com.izapolsky.distance.search.services;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;

import static com.izapolsky.distance.search.services.H2GisStorageServiceTest.getCustomer;
import static org.junit.Assert.*;

public class CustomerRendererImplTest {

    private CustomerRendererImpl toTest = new CustomerRendererImpl();

    @Test
    public void testRender() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pw = new PrintStream(baos);
        toTest.renderTo(Arrays.asList(getCustomer("One Fella", 100L, null, null)), pw);
        pw.close();

        assertEquals("One Fella\t100\n", new String(baos.toByteArray()));
    }

    @Test
    public void testRenderEmpty() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pw = new PrintStream(baos);
        toTest.renderTo(Collections.emptyList(), pw);
        pw.close();

        assertEquals("", new String(baos.toByteArray()));
    }

}