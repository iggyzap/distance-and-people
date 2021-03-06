package com.izapolsky.distance.search.services;

import com.izapolsky.distance.search.api.Customer;
import com.izapolsky.distance.search.api.Query;

/**
 * A service that encapsulates how customer data can processed and queried
 */
public interface StorageService {

    /**
     * Attempts to add given customers to the storage
     * @param customers iterable over customers
     * @param discardPrevious if old records can be dropped upon import
     * @return number of records that were added to storage
     */
    Long batchProcessCustomers(Iterable<Customer> customers, boolean discardPrevious);

    /**
     * Locates matching customers from underlying storage
     * @param cleaner An observer that allows for precise control of resource clean-up
     * @param query parameters to do filtering
     * @return a search result
     */
    Iterable<Customer> findMatching(ResourceCleaner cleaner, Query query);




}
