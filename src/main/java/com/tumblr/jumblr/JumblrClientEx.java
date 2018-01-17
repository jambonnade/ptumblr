package com.tumblr.jumblr;

import com.tumblr.jumblr.request.RequestBuilderEx;
import java.util.HashMap;
import java.util.Map;

/**
 * Replacement of JumblrClient to use the new RequestBuilder and get 
 * API call response headers
 */
public class JumblrClientEx extends JumblrClient {
    private Map<String, String> lastResponseHeaders;
    private String lastResponseBody;


    public JumblrClientEx(String consumerKey, String consumerSecret) {
        super(consumerKey, consumerSecret);
        RequestBuilderEx rb = new RequestBuilderEx(this);
        rb.setConsumer(consumerKey, consumerSecret);
        setRequestBuilder(rb);
    }

    public Map<String, String> getLastResponseHeaders() {
        return lastResponseHeaders;
    }

    public void setLastResponseHeaders(Map<String, String> lastResponseHeaders) {
        this.lastResponseHeaders = lastResponseHeaders;
    }

    public String getLastResponseBody() {
        return lastResponseBody;
    }

    public void setLastResponseBody(String lastResponseBody) {
        this.lastResponseBody = lastResponseBody;
    }
    
    
    /**
     * Similar to postReblog() but doesn't fetch newly created post data.
     * postReblog() is bugged and can't be used.
     * 
     * @param blogName
     * @param postId
     * @param reblogKey
     * @return Created tumblr post id
     */
    public Long reblog(String blogName, Long postId, String reblogKey) {
        Map<String, Object> soptions = new HashMap<>();
        soptions.put("id", postId.toString());
        soptions.put("reblog_key", reblogKey);
        return getRequestBuilder().post(JumblrClientEx.blogPath(blogName, "/post/reblog"), soptions).getId();
    }

    
    
    // Copy/paste of private utility methods in JumblrClient
    private static String blogPath(String blogName, String extPath) {
        return "/blog/" + blogUrl(blogName) + extPath;
    }

    private static String blogUrl(String blogName) {
        return blogName.contains(".") ? blogName : blogName + ".tumblr.com";
    }


}
