package com.stacktrace.yo.plex4j.api;

import com.stacktrace.yo.plex4j.domain.PlexServer;

public enum Route {

    LOGIN("https://plex.tv/users/sign_in") {
        @Override
        public boolean serverRoute() {
            return false;
        }

        @Override
        public String jsonPath() {
            return this.baseUrl() + ".json/";
        }

        @Override
        public String xmlPath() {
            return this.baseUrl() + ".xml/";
        }
    };

    private final String path;

    Route(String path) {
        this.path = path;
    }

    public PlexRoute createXML(PlexServer server) {
        return new PlexRoute(this, server, PlexRoute.ResponseType.XML);
    }

    public PlexRoute create(PlexServer server) {
        return new PlexRoute(this, server);
    }

    public String baseUrl() {
        return path;
    }

    public String jsonPath() {
        return path;
    }

    public String xmlPath() {
        return path;
    }

    public boolean serverRoute() {
        return true;
    }
}
