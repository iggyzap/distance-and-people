package com.izapolsky.distance.search;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MainIntegrationTest {

    private PrintStream io = System.out;
    private PrintStream err = System.err;
    private InputStream original = System.in;

    private ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
    private ByteArrayOutputStream bytesErrOut = new ByteArrayOutputStream();


    @Before
    public void setUp() {
        System.setOut(new PrintStream(bytesOut));
        System.setErr(new PrintStream(bytesErrOut));
    }

    @After
    public void tearDown() {
        System.setOut(io);
        System.setErr(err);
        System.setIn(original);
    }

    @Test
    public void testRun() throws IOException {

        System.setIn(getClass().getResourceAsStream("/customers.json"));

        Main.main();

        System.out.close();

        assertEquals(Arrays.asList("Ian Kehoe\t4", "Nora Dempsey\t5","Theresa Enright\t6","Eoin Ahearn\t8",
                "Richard Finnegan\t11", "Christina McArdle\t12","Olive Ahearn\t13","Michael Ahearn\t15","Patricia Cahill\t17",
                "Eoin Gallagher\t23", "Rose Enright\t24", "Stephen McArdle\t26","Oliver Ahearn\t29",
                "Nick Enright\t30","Alan Behan\t31","Lisa Ahearn\t39"), IOUtils.readLines(new ByteArrayInputStream(bytesOut.toByteArray())));
    }


}