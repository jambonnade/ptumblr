package de.jambonna.ptumblr.web;

import java.io.Serializable;
import org.springframework.social.oauth1.OAuthToken;

/**
 *
 */
public class UserSession implements Serializable {
    private static long serialVersionUID = 1L;
    
    private Long userId;
    private OAuthToken tumblrRequestToken;
    private OAuthToken tumblrAccessToken;
    private Boolean justLoggedIn;
    private Boolean justLoggedBack;
    private Long justLikedPostId;
    private Long justRebloggedPostId;
    

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public OAuthToken getTumblrRequestToken() {
        return tumblrRequestToken;
    }

    public void setTumblrRequestToken(OAuthToken tumblrRequestToken) {
        this.tumblrRequestToken = tumblrRequestToken;
    }
    
    public OAuthToken getTumblrAccessToken() {
        return tumblrAccessToken;
    }

    public void setTumblrAccessToken(OAuthToken tumblrAccessToken) {
        this.tumblrAccessToken = tumblrAccessToken;
    }

    
    
    
    public Boolean getJustLoggedIn() {
        return justLoggedIn;
    }

    public Boolean consumeJustLoggedIn() {
        Boolean result = getJustLoggedIn();
        justLoggedIn = null;
        return result;
    }

    public void setJustLoggedIn(Boolean justLoggedIn) {
        this.justLoggedIn = justLoggedIn;
    }

    public Boolean getJustLoggedBack() {
        return justLoggedBack;
    }
    public Boolean consumeJustLoggedBack() {
        Boolean result = getJustLoggedBack();
        justLoggedBack = null;
        return result;
    }

    public void setJustLoggedBack(Boolean justLoggedBack) {
        this.justLoggedBack = justLoggedBack;
    }

    public Long getJustLikedPostId() {
        return justLikedPostId;
    }
    public Long consumeJustLikedPostId() {
        Long result = getJustLikedPostId();
        justLikedPostId = null;
        return result;
    }

    public void setJustLikedPostId(Long justLikedPostId) {
        this.justLikedPostId = justLikedPostId;
    }

    public Long getJustRebloggedPostId() {
        return justRebloggedPostId;
    }
    public Long consumeJustRebloggedPostId() {
        Long result = getJustRebloggedPostId();
        justRebloggedPostId = null;
        return result;
    }

    public void setJustRebloggedPostId(Long justRebloggedPostId) {
        this.justRebloggedPostId = justRebloggedPostId;
    }
    
    
}
