package com.stacktrace.yo.plex4j.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ServerAuth {

    private String token;
    private String username;
    private String password;


}
