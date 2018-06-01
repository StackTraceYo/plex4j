package com.stacktrace.yo.plex4j.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.stacktrace.yo.plex4j.api.plex.PlexServer;
import com.stacktrace.yo.plex4j.api.plex.ServerAuth;
import com.stacktrace.yo.plex4j.api.plex.ServerLocation;
import com.stacktrace.yo.plex4j.api.route.Route;
import com.stacktrace.yo.plex4j.model.PlexUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlexClient {

    private final Map<String, PlexServer> servers;
    private final PlexHttpClient httpClient;
    private final ObjectMapper oMapper = new ObjectMapper();
    private final XmlMapper xMapper = new XmlMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(PlexClient.class);

    public PlexClient() {
        this.servers = new HashMap<>();
        this.httpClient = new PlexHttpClient();
    }

    public PlexClient(PlexHttpClient client) {
        this.servers = new HashMap<>();
        this.httpClient = client;
    }

    public PlexClient addServer(PlexServer server) {
        if (server != null) {
            if (this.connect(server)) {
                this.servers.put(server.getName(), server);
                LOGGER.debug("Server Added");
            } else {
                LOGGER.warn("Server Did Not Connect {}", server);
            }
        }
        return this;
    }

    private boolean connect(PlexServer server) {
        if (server.getAuthentication().shouldLogin()) {
            Optional<PlexUser> loginResponse = this.httpClient.post(
                    Route.LOGIN.create(server),
                    server.serverLoginParams(),
                    server.createClientIdentifier(),
                    response -> oMapper.readValue(response, PlexUser.class)
            );
            return loginResponse.map(o -> {
                String authToken = o.getUser().getAuthenticationToken();
                LOGGER.debug("Login Successful - setting token", authToken);
                server.getAuthentication().setToken(authToken);
                Object x = this.httpClient.get(
                        Route.SEARCH.create(server),
                        server.createDefaultHeaders(),
                        response -> oMapper.readValue(response, Object.class)
                );
                LOGGER.debug(x.toString());
                return true;
            }).orElse(false);
        } else return server.getAuthentication().getToken() != null;

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
