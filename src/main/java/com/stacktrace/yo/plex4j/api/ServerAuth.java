package com.stacktrace.yo.plex4j.api;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@Data
@Accessors(chain = true)
public class ServerAuth {

    private String token;
    private String username;
    private String password;


    public NameValuePair[] createLoginParams() {
        return new NameValuePair[]{
                new BasicNameValuePair("user[login]", username),
                new BasicNameValuePair("user[password]", password)
        };
    }

    public boolean shouldLogin() {
        return this.token == null || this.token.isEmpty();
    }


}
