package com.stacktrace.yo.plex4j.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stacktrace.yo.plex4j.api.route.PlexRoute;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

public class PlexHttpClient {

    private final ObjectMapper oMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(PlexHttpClient.class);


    public <T> Optional<T> post(PlexRoute pRoute, Object request, PlexResponseMapping<T> mapper) {

        try {
            String rq = oMapper.writeValueAsString(request);

            URI route = pRoute.toUri();
            HttpPost httpPost = new HttpPost(route);
            httpPost.setEntity(
                    EntityBuilder
                            .create()
                            .setContentType(ContentType.APPLICATION_JSON)
                            .setText(rq)
                            .build()
            );
            T result = executePost(httpPost, new PlexResponseHandler(), mapper);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", pRoute, e);
            return Optional.empty();
        }
    }

    public <T> Optional<T> post(PlexRoute pRoute, NameValuePair[] params, NameValuePair[] headers, PlexResponseMapping<T> mapper) {

        try {

            URI route = pRoute.toUriWithParams(params);
            HttpPost httpPost = new HttpPost(route);
            for (NameValuePair pair : headers) {
                httpPost.addHeader(pair.getName(), pair.getValue());
            }
            T result = executePost(httpPost, new PlexResponseHandler(), mapper);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", pRoute, e);
            return Optional.empty();
        }
    }

    public <T> Optional<T> get(PlexRoute pRoute, NameValuePair[] headers, PlexResponseMapping<T> mapper) {
        try {
            URI route = pRoute.toUri();
            HttpGet get = new HttpGet(route);
            for (NameValuePair pair : headers) {
                get.addHeader(pair.getName(), pair.getValue());
            }
            T result = executeGet(get, new PlexResponseHandler(), mapper);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", pRoute, e);
            return Optional.empty();
        }
    }

    private <T> T executeGet(HttpGet get, ResponseHandler<String> handler, PlexResponseMapping<T> mapper) {
        try (CloseableHttpClient httpClient = httpClient()) {
            LOGGER.debug("{} {} {}", get.getMethod(), get.getURI());
            String responseBody = httpClient.execute(get, handler);
            LOGGER.debug("Response Body: {}", responseBody);
            return mapper.createFrom(responseBody);
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", get.getURI(), e);
            return null;
        }
    }

    private <T> T executePost(HttpPost post, ResponseHandler<String> handler, PlexResponseMapping<T> mapper) {
        try (CloseableHttpClient httpClient = httpClient()) {
            LOGGER.debug("{} {}", post.getMethod(), post.getURI());
            String responseBody = httpClient.execute(post, handler);
            LOGGER.debug("Response Body: {}", responseBody);
            return mapper.createFrom(responseBody);
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", post.getURI(), e);
            return null;
        }
    }

    public static class PlexResponseHandler implements ResponseHandler<String> {

        private static final Logger LOGGER = LoggerFactory.getLogger(PlexResponseHandler.class);

        @Override
        public String handleResponse(HttpResponse httpResponse) throws IOException {
            int status = httpResponse.getStatusLine().getStatusCode();
            LOGGER.debug("HTTP status code from Request = {}", status);
            if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
                HttpEntity entity = httpResponse.getEntity();
                return entity == null ? null : EntityUtils.toString(entity);
            } else {
                throw new RuntimeException(
                        "Unexpected status from response - " + status
                );
            }
        }
    }

    private CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

    @FunctionalInterface
    public interface PlexResponseMapping<T> {

        T createFrom(String responseBody) throws IOException;
    }
}


