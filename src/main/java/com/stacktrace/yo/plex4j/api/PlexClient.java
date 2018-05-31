package com.stacktrace.yo.plex4j.api;

import com.stacktrace.yo.plex4j.domain.PlexServer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PlexClient {

    private final Map<String, PlexServer> servers;
    private final PlexHttpClient client;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlexClient.class);

    public PlexClient() {
        this.servers = new HashMap<>();
        this.client = new PlexHttpClient();
    }

    public PlexClient(PlexHttpClient client) {
        this.servers = new HashMap<>();
        this.client = client;
    }

    public PlexClient addServer(PlexServer server) {
        if (server != null) {
            this.connect(server);
        }
        this.servers.put(server.getName(), server);
        return this;
    }

    private boolean connect(PlexServer server) {
        if (server.getAuthentication().shouldLogin()) {
            PlexRoute login = Route.LOGIN.createXML(server);
            NameValuePair[] params = server.getAuthentication().createLoginParams();
            Object x = this.client.postX(login, params, new NameValuePair[]{new BasicNameValuePair("X-Plex-Client-Identifier", "ahmadfaragtest")}, Object.class);
            LOGGER.debug(x.toString());
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {


        PlexClient client = new PlexClient()
                .addServer(new PlexServer()
                        .setName("test")
                        .setAuthentication(new ServerAuth())
                        .setLocation(new ServerLocation()
                                .setHost("75.118.77.167")
                                .setPort("32400")));
    }
}
