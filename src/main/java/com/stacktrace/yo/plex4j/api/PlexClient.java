package com.stacktrace.yo.plex4j.api;

import com.stacktrace.yo.plex4j.domain.PlexServer;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlexClient {

    private final CloseableHttpClient http;
    private final Map<String, PlexServer> servers;

    public PlexClient() {
        this.http = HttpClients.createDefault();
        this.servers = new HashMap<>();
    }

    public PlexClient addServer(PlexServer server) {

        this.servers.put(server.getName(), server);
        return this;
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
        HttpGet get = new HttpGet(servers.get("test").location());
        get.addHeader("Accept", "application/json");
        get.addHeader("X-PlexServer-Token", "nacWLAopife2xy4r2ABQ");

        String x = this.http.execute(get, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(x);
    }

    public static void main(String[] args) throws IOException {


        PlexClient client = new PlexClient()
                .addServer(new PlexServer()
                        .setAuthentication(new ServerAuth()
                                .setUsername("stacktraceyo")
                                .setPassword("Ilovemymom123"))
                        .setLocation(new ServerLocation()
                                .setHost("75.118.77.167")
                                .setPort("32400")));
        client.test();
    }
}
