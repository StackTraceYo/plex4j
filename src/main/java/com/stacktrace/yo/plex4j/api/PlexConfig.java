package com.stacktrace.yo.plex4j.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlexConfig {

    private String host;
    private String port;
    private String name;
    private String basePath;

    public String getLocation() {
        if (basePath == null) {
            basePath = "http://" + host + ":" + port;
        }
        return basePath;
    }
}
