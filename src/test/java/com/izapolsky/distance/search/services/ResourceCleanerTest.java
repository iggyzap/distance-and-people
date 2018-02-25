package com.izapolsky.distance.search.services;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.verify;

public class ResourceCleanerTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Mock
    private AutoCloseable mockCloseable;

    @Test
    public void testCloseOneAllowsNullsAndExceptions() throws Exception {
        Mockito.doThrow(new Exception("Bad exception")).when(mockCloseable).close();

        try (ResourceCleaner toTest = new ResourceCleaner()) {
            toTest.registerResource(null);
            toTest.registerResource(mockCloseable);
            toTest.registerResource(mockCloseable);
        }
        verify(mockCloseable, Mockito.times(2)).close();
    }

}