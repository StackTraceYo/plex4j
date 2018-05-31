package com.stacktrace.yo.plex4j.api.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlexLoginRequest {

    private String username;
    private String password;
}
