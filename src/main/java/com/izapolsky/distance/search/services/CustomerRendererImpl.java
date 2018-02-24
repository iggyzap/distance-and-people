package com.izapolsky.distance.search.services;

import com.izapolsky.distance.search.api.Customer;

import java.io.PrintStream;


public class CustomerRendererImpl implements CustomerRenderer {
    @Override
    public void renderTo(Iterable<Customer> customers, PrintStream to) {
        for(Customer customer: customers) {
            to.print(customer.name);
            to.print("\t");
            to.println(customer.userId);
        }
    }
}
