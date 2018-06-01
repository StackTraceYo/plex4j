package com.stacktrace.yo.plex4j.api.route;


import com.stacktrace.yo.plex4j.api.plex.PlexServer;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class PlexRoute {

    public enum ResponseType {
        XML() {
            @Override
            public String getPathResponseType(Route route) {
                return route.xmlPath();
            }
        },
        JSON() {
            @Override
            public String getPathResponseType(Route route) {
                return route.jsonPath();
            }
        };

        abstract public String getPathResponseType(Route route);
    }

    private final Route route;
    private final PlexServer server;
    private final ResponseType responseType;

    public PlexRoute(Route route, PlexServer server) {
        this(route, server, ResponseType.JSON);
    }

    public PlexRoute(Route route, PlexServer server, ResponseType type) {
        this.route = route;
        this.server = server;
        this.responseType = type;
    }

    public URI toUri() throws URISyntaxException {
        if (route.serverRoute()) {
            return new URI(server.location() + responseType.getPathResponseType(route));
        } else {
            return new URI(responseType.getPathResponseType(route));
        }
    }

    public URI toUriWithParams(NameValuePair[] params) throws URISyntaxException {
        URIBuilder builder;
        if (route.serverRoute()) {
            builder = new URIBuilder(server.location() + responseType.getPathResponseType(route));
        } else {
            builder = new URIBuilder(responseType.getPathResponseType(route));
        }
        for (NameValuePair pair : params) {
            builder.addParameter(pair.getName(), pair.getValue());
        }
        return builder.build();
    }

}
