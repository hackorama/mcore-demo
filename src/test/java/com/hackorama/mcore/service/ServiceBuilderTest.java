package com.hackorama.mcore.service;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.hackorama.mcore.data.DataStore;
import com.hackorama.mcore.data.redis.RedisDataStoreCacheQueue;
import com.hackorama.mcore.server.spark.SparkServer;
import com.hackorama.mcore.service.group.GroupService;

public class ServiceBuilderTest {

    @Test
    public void service_dynamicOrchestration_expectsNoErrors() throws FileNotFoundException, IOException {
        DataStore store = new RedisDataStoreCacheQueue();
        Service service = new GroupService().configureUsing(new SparkServer("test")).configureUsing(store)
                .configureUsing(store.asQueue()).configureUsing(store.asCache()).start();
        assertNotNull(service);
        service.stop();
    }

}
