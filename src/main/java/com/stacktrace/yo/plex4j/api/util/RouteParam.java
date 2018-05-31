package com.stacktrace.yo.plex4j.api.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.http.NameValuePair;

@Data()
@Accessors(chain = true)
@AllArgsConstructor
public class RouteParam implements NameValuePair {
    private final String name;
    private final String value;
}
