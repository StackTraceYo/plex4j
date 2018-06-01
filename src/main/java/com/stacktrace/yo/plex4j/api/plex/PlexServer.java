package com.stacktrace.yo.plex4j.api.plex;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@Data
@Accessors(chain = true)
public class PlexServer {

    private String name;
    private ServerLocation location;
    private ServerAuth authentication;

    public String location() {
        return location.location();
    }

    public NameValuePair[] serverLoginParams() {
        return authentication.createLoginParams();
    }

    public NameValuePair[] createClientIdentifier() {
        return new NameValuePair[]{
                new BasicNameValuePair("X-Plex-Client-Identifier", "Plex4J-" + name)
        };
    }

    public NameValuePair[] createDefaultHeaders() {
        return new NameValuePair[]{
                new BasicNameValuePair("Accept", "application/json"),
                new BasicNameValuePair("X-Plex-Token", authentication.getToken())
        };
    }
}
