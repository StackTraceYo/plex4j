package com.stacktrace.yo.plex4j.api;

public enum Route {

    LOGIN("https://plex.tv/users/sign_in.xml");


    private final String base;

    Route(String route) {
        this.base = route;
    }

    public PlexRoute create() {
        return new PlexRoute(this);
    }

}
