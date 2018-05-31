package com.stacktrace.yo.plex4j.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ServerLocation {

    private String host;
    private String port;
    private String basePath;

    public String location() {
        if (basePath == null) {
            basePath = "http://" + host + ":" + port;
        }
        return basePath;
    }
}
