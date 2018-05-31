package com.stacktrace.yo.plex4j.api;


import com.stacktrace.yo.plex4j.api.util.RouteParam;

import java.util.ArrayList;
import java.util.List;

public class PlexRoute {

    private final Route route;
    private final List<RouteParam> params;

    public PlexRoute(Route route) {
        this.route = route;
        params = new ArrayList<>();
    }



}
