package com.izapolsky.distance.search.services;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that serves as safe registry for resources to be cleaned up at some stage
 */
public class ResourceCleaner implements AutoCloseable {

    private final List<AutoCloseable> registeredResources = new ArrayList<>(2);

    /**
     * Registers a given resource for closing
     * @param resource
     * @param <T>
     */
    public <T extends AutoCloseable> void registerResource(T resource) {
        registeredResources.add(resource);
    }

    @Override
    public void close() {

        for (AutoCloseable ac : registeredResources) {
            try {
                if (ac != null) {
                    ac.close();
                }
            } catch (Exception e) {
                //ignore exception
            }
        }

    }
}
