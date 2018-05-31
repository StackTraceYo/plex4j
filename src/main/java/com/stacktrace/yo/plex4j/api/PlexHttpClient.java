package com.stacktrace.yo.plex4j.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PlexHttpClient {

    private final CloseableHttpClient client;
    private final ObjectMapper oMapper = new ObjectMapper();
    private final XmlMapper xMapper = new XmlMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(PlexHttpClient.class);


    public PlexHttpClient() {
        this.client = HttpClients.createDefault();
    }

    public PlexHttpClient(CloseableHttpClient client) {
        this.client = client;
    }


    public <T> T post(PlexRoute pRoute, Object request, Class<T> resType) {

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
            return executePost(httpPost, new PlexResponseHandler(), responseBody -> oMapper.readValue(responseBody, resType));
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", pRoute, e);
            return null;
        }
    }

    public <T> T post(PlexRoute pRoute, NameValuePair[] params, NameValuePair[] headers, Class<T> resType) {

        try {

            URI route = pRoute.toUriWithParams(params);
            HttpPost httpPost = new HttpPost(route);
            for (NameValuePair pair : headers) {
                httpPost.addHeader(pair.getName(), pair.getValue());
            }
            return executePost(httpPost, new PlexResponseHandler(), responseBody -> oMapper.readValue(responseBody, resType));
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", pRoute, e);
            return null;
        }
    }

    public <T> T get(PlexRoute pRoute, NameValuePair[] params, Class<T> resType) {

        try {
            URI route = pRoute.toUriWithParams(params);
            HttpGet get = new HttpGet(route);
            return executeGet(get, new PlexResponseHandler(), responseBody -> oMapper.readValue(responseBody, resType));
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", pRoute, e);
            return null;
        }
    }


    public <T> T postX(PlexRoute pRoute, Object request, Class<T> resType) {

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
            return executePost(httpPost, new PlexResponseHandler(), responseBody ->
                    xMapper.readValue(responseBody, resType)
            );
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", pRoute, e);
            return null;
        }
    }

    public <T> T postX(PlexRoute pRoute, NameValuePair[] params, NameValuePair[] headers, Class<T> resType) {

        try {

            URI route = pRoute.toUriWithParams(params);
            HttpPost httpPost = new HttpPost(route);
            for (NameValuePair pair : headers) {
                httpPost.addHeader(pair.getName(), pair.getValue());
            }
            return executePost(httpPost, new PlexResponseHandler(), responseBody ->
                    xMapper.readValue(responseBody, resType)
            );
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", pRoute, e);
            return null;
        }
    }

    public <T> T getX(PlexRoute pRoute, NameValuePair[] params, Class<T> resType) {

        try {
            URI route = pRoute.toUriWithParams(params);
            HttpGet get = new HttpGet(route);
            return executeGet(get, new PlexResponseHandler(), responseBody -> xMapper.readValue(responseBody, resType));
        } catch (Exception e) {
            LOGGER.debug("Exception calling {}", pRoute, e);
            return null;
        }
    }

    private <T> T executeGet(HttpGet get, ResponseHandler<String> handler, PlexResponseMapping<T> mapper) {
        try (CloseableHttpClient httpClient = client) {
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
        try (CloseableHttpClient httpClient = client) {
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

    @FunctionalInterface
    public interface PlexResponseMapping<T> {

        T createFrom(String responseBody) throws IOException;
    }

    public static void main(String args[]) throws URISyntaxException, IOException {
        PlexHttpClient client = new PlexHttpClient();
        HttpPost get = new HttpPost(new URIBuilder("https://plex.tv/users/sign_in.xml/").addParameter("user[login]", "stacktraceyo").addParameter("user[password]", "Ilovemymom123").build());
        get.addHeader("X-Plex-Client-Identifier", "ahmadfaragtest");
        LOGGER.debug(get.toString());
        String responseBody = client.client.execute(get, new PlexResponseHandler());
        LOGGER.debug(responseBody);
    }
}


