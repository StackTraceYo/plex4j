package com.stacktrace.yo.plex4j.domain;

import com.stacktrace.yo.plex4j.api.ServerAuth;
import com.stacktrace.yo.plex4j.api.ServerLocation;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlexServer {

    private String name;
    private ServerLocation location;
    private ServerAuth authentication;

    public String location(){
        return location.location();
    }
}
