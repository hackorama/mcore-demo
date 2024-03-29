package com.hackorama.mcore.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

import com.hackorama.mcore.client.unirest.CachingUnirestClient;
import com.hackorama.mcore.client.unirest.UnirestClient;
import com.hackorama.mcore.common.Response;
import com.hackorama.mcore.common.TestUtil;
import com.hackorama.mcore.common.Util;

/**
 * Tests for Workspace service
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public class ClientTest {

    private static final String DEFAULT_SERVER_ENDPOINT = TestUtil.defaultServerEndpoint();

    @Before
    public void setUp() throws Exception {
        TestUtil.initServiceInstance();
    }

    @After
    public void tearDown() throws Exception {
        TestUtil.clearDataOfServiceInstance();
    }

    @AfterClass
    public static void afterAllTests() throws Exception {
        TestUtil.stopServiceInstance();
    }

    @Test
    public void client_getResources_expectsEmptyResourcesListWithOKStatus() {
        client_getResources_expectsEmptyResourcesListWithOKStatus(new UnirestClient());
        client_getResources_expectsEmptyResourcesListWithOKStatus(new CachingUnirestClient());
    }

    private void client_getResources_expectsEmptyResourcesListWithOKStatus(Client client) {
        Response response = client.get(DEFAULT_SERVER_ENDPOINT + "/workspace");
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());
        assertEquals("[]", response.getBody());
    }


    @Test
    public void client_postResource_expectsMatchingResourceReturnedWithOKStatus() {
        client_postResource_expectsMatchingResourceReturnedWithOKStatus(new UnirestClient());
        client_postResource_expectsMatchingResourceReturnedWithOKStatus(new CachingUnirestClient());
    }

    public void client_postResource_expectsMatchingResourceReturnedWithOKStatus(Client client) {
        Response response = client.post(DEFAULT_SERVER_ENDPOINT + "/workspace", "{ \"name\" : \"one\" }");
        JsonObject bodyJson = Util.toJsonObject(response.getBody());
        System.out.println(response.getBody());
        System.out.println(response.getStatus());
        response = client.get(DEFAULT_SERVER_ENDPOINT + "/workspace/" + bodyJson.get("id").getAsString());
        System.out.println(response.getBody());
        System.out.println(response.getStatus());
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());
        assertTrue(response.getBody().contains("one"));
    }

    @Test
    public void client_invalidURL_expectsErrorMessage() {
        client_invalidURL_expectsErrorMessage(new UnirestClient());
        client_invalidURL_expectsErrorMessage(new CachingUnirestClient());
    }

    public void client_invalidURL_expectsErrorMessage(Client client) {
        Response response = client.get("https://localhost/fail");
        assertEquals(0, response.getStatus());
        assertTrue(response.getBody().contains("error"));
    }

}
