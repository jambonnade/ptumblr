package de.jambonna.ptumblr.app;

import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.web.MainController;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 */
@Component
public class Urls {
    private final UrlBuilder urlBuilder;

    
    public Urls(UrlBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }
    
    
    public String getDashboardUrl() {
        return urlBuilder.fromController(MainController.class)
                .path("/dashboard").toUriString();
    }

    public String getDashboardUrl(Long topPostId, boolean bottomPostAnchor) {
        UriComponentsBuilder b = urlBuilder.fromController(MainController.class)
                .path("/dashboard/to/{tumblrId}");
        if (bottomPostAnchor) {
            b.fragment("bottom-post");
        }
        return b.buildAndExpand(topPostId).toUriString();
    }
    
    public String getDashboardUrl(Long topPostId) {
        return getDashboardUrl(topPostId, false);
    }
    public String getDashboardUrl(Post topPost) {
        return getDashboardUrl(topPost.getTumblrId(), false);
    }
    public String getDashboardBottomPostUrl(Post topPost) {
        return getDashboardUrl(topPost.getTumblrId(), true);
    }
    
    public String getLikePostUrl(Post post) {
        if (post.getPostId() == null) {
            throw new IllegalArgumentException("Invalid post");
        }
        return urlBuilder.fromController(MainController.class)
                .path("/post/{postId}/like")
                .buildAndExpand(post.getPostId())
                .toUriString();
    }
    
    public String getReblogPostUrl(Post post) {
        if (post.getPostId() == null) {
            throw new IllegalArgumentException("Invalid post");
        }
        return urlBuilder.fromController(MainController.class)
                .path("/post/{postId}/reblog")
                .buildAndExpand(post.getPostId())
                .toUriString();
    }

    public String getDashboardFetchNewUrl() {
        return urlBuilder.fromController(MainController.class)
                .path("/dashboard/fetchnew").toUriString();
    }

    public String getDashboardResetUrl() {
        return urlBuilder.fromController(MainController.class)
                .path("/dashboard/reset").toUriString();
    }

    public String getOAuthConnectUrl() {
        return urlBuilder.fromController(MainController.class)
                .path("/oauthconnect").toUriString();
    }

    public String getOAuthVerifyUrl() {
        return urlBuilder.fromController(MainController.class)
                .path("/oauthverify").toUriString();
    }
    
    public String getDisconnectUrl() {
        return urlBuilder.fromController(MainController.class)
                .path("/disconnect").toUriString();
    }

    public String getApiInfoUrl() {
        return urlBuilder.fromController(MainController.class)
                .path("/api").toUriString();
    }

    public String getBaseUrl() {
        String baseUrl = urlBuilder.getBaseUrl();
        return baseUrl.length() > 0 ? baseUrl : "/";
    }
}
