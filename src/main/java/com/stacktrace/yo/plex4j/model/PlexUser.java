package com.stacktrace.yo.plex4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlexUser {

    @JsonProperty("user")
    private User user;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("uuid")
        private String uuid;
        @JsonProperty("email")
        private String email;
        @JsonProperty("joined_at")
        private String joinedAt;
        @JsonProperty("username")
        private String username;
        @JsonProperty("title")
        private String title;
        @JsonProperty("thumb")
        private String thumb;
        @JsonProperty("hasPassword")
        private Boolean hasPassword;
        @JsonProperty("authToken")
        private String authToken;
        @JsonProperty("authentication_token")
        private String authenticationToken;
        @JsonProperty("subscription")
        private Subscription subscription;
        @JsonProperty("roles")
        private Roles roles;
        @JsonProperty("entitlements")
        private List<String> entitlements;
        @JsonProperty("confirmedAt")
        private Object confirmedAt;
        @JsonProperty("forumId")
        private Integer forumId;
        @JsonProperty("rememberMe")
        private Boolean rememberMe;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Subscription {

        @JsonProperty("active")
        private Boolean active;
        @JsonProperty("status")
        private String status;
        @JsonProperty("plan")
        private String plan;
        @JsonProperty("features")
        private List<String> features;

    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Roles {

        @JsonProperty("roles")
        private List<String> roles;

    }
}
