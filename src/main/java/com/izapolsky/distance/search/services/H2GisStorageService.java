package com.izapolsky.distance.search.services;

import com.google.common.collect.Iterables;
import com.izapolsky.distance.search.api.Customer;
import com.izapolsky.distance.search.api.Query;
import org.h2.Driver;
import org.h2gis.ext.H2GISExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * A version that uses h2gis extension to h2 database to store geospatial data and run queries
 */
public class H2GisStorageService implements StorageService {
    private static final int BATCH_SIZE = 100;

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load db driver", e);
        }
    }

    private String dbConnectionName = "jdbc:h2:mem:customers;DB_CLOSE_DELAY=-1";
    private String DB_USER = "sa";
    private String DB_PASSWORD = "sa";
    private Logger logger = LoggerFactory.getLogger(getClass());

    public H2GisStorageService() {
        withDbConnection(connection -> {
            H2GISExtension.load(connection);
            Statement st = connection.createStatement();
            st.execute("create table if not EXISTS customers (name varchar not null, user_id numeric (19,0) not null, location GEOMETRY not null);");
            st.execute("create index if not exists idx_customers_user_id on customers(user_id);");

            return null;
        });
    }

    @Override
    public Long batchProcessCustomers(Iterable<Customer> customers, boolean discardPrevious) {
        long processed = 0;

        //logic here : truncate customers table if needed. run batch insert into h2 converting from
        // longitude+latitude coordinates into cartesian space
        if (discardPrevious) {
            withDbConnection(c -> c.createStatement().execute("truncate table customers;"));
        }


        processed = withDbConnection(connection -> {
            PreparedStatement s = connection.prepareStatement("insert into customers (name, user_id, location) values (?,?, ST_Transform(ST_GeomFromText(" +
                    " ? , 4326), 3035))");
            AtomicLong totalRecords = new AtomicLong(0L);
            Iterables.partition(customers, BATCH_SIZE).forEach(pageOfCustomers -> {
                totalRecords.addAndGet(pageOfCustomers.size());
                for (Customer customer : pageOfCustomers) {
                    bindCustomer(customer, s);
                }
            });
            long inserts = 0;
            for (int i: handleBatchUpdateException(s)) {
                inserts+= i >= 0 ? i : 0;
            }
            long badRecords = totalRecords.get() - inserts;
            if (badRecords > 0) {
                logger.warn("Encountered {} bad records out of {} in total", badRecords, totalRecords.get());
            }
            return inserts;
        });


        return processed;
    }

    protected int[] handleBatchUpdateException (PreparedStatement pst) throws SQLException{
        int[] results;
        try {
            results = pst.executeBatch();
        } catch (BatchUpdateException be) {
            results = be.getUpdateCounts();
            logger.warn("Batch update encountered number of errors, me: ", be);
        }

        return results;
    }

    private void bindCustomer(Customer customer, PreparedStatement s) {
        try {
            s.setObject(1, customer.name);
            s.setObject(2, customer.userId);
            //this is how sql injection can happen
            String point = null;
            if (customer.longitude != null && customer.latitude != null) {
                point = "POINT( " + customer.latitude + " " + customer.longitude + ")";
            }
            s.setString(3, point) ;
            s.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed processing customer ", e);
        }
    }

    @FunctionalInterface
    public interface SqlFunction <C extends Connection, T> {
        T apply(C c) throws SQLException;
    }

    protected <T> T withDbConnection(SqlFunction<Connection, T> fn) {
        try (Connection c =  DriverManager.getConnection(dbConnectionName, DB_USER, DB_PASSWORD)) {
            return fn.apply(c);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to working with jdbc", e);
        }
    }

    @Override
    public Iterable<Customer> findMatching(Query query) {
        return Collections.emptyList();
    }
}
