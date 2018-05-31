package com.stacktrace.yo.plex4j.api;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class PlexClient {

    private final PlexConfig config;
    private final CloseableHttpClient http;

    public PlexClient(PlexConfig config) {
        this.config = config;
        this.http = HttpClients.createDefault();
    }

    public void test() throws IOException {

        ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        HttpGet get = new HttpGet(config.getLocation());
        get.addHeader("Accept", "application/json");
        get.addHeader("X-Plex-Token", "nacWLAopife2xy4r2ABQ");

        String x = this.http.execute(get, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(x);
    }

    public static void main(String[] args) throws IOException {


        PlexClient client = new PlexClient(
                new PlexConfig()
                        .setHost("75.118.77.167")
                        .setPort("32400")
                        .setName("thestack")
        );
        client.test();
    }
}
