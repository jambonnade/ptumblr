package com.tumblr.jumblr.request;

import com.tumblr.jumblr.JumblrClientEx;
import com.tumblr.jumblr.responses.ResponseWrapper;
import org.scribe.model.Response;


/**
 * Replacement of Jumblr's RequestBuilder class to be able to get response headers
 */
public class RequestBuilderEx extends RequestBuilder {

    private final JumblrClientEx jambonnadeClient;
    
    public RequestBuilderEx(JumblrClientEx client) {
        super(client);
        jambonnadeClient = client;
    }

    
    @Override
    ResponseWrapper clear(Response response) {
        ResponseWrapper w = super.clear(response);
        jambonnadeClient.setLastResponseHeaders(response.getHeaders());
        jambonnadeClient.setLastResponseBody(response.getBody());
        return w;
    }

}
