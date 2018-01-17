package de.jambonna.ptumblr.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.springframework.social.oauth1.OAuthToken;

/**
 *
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    private String username;
    
    private String accessToken;
    private String accessTokenSecret;
    
    private Long lastViewedTopPostId;
    private Long topmostViewedPostId;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstLogin;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;
    
    @Transient
    private Boolean newUser;

    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }
    
    public boolean hasAccesToken() {
        return getAccessToken() != null && getAccessToken().length() > 0
                && getAccessTokenSecret() != null 
                && getAccessTokenSecret().length() > 0;
    }
    
    public void setAccessToken(OAuthToken token) {
        setAccessToken(token.getValue());
        setAccessTokenSecret(token.getSecret());
    }

    public Long getLastViewedTopPostId() {
        return lastViewedTopPostId;
    }

    public void setLastViewedTopPostId(Long lastViewedTopPostId) {
        this.lastViewedTopPostId = lastViewedTopPostId;
    }

    public Long getTopmostViewedPostId() {
        return topmostViewedPostId;
    }

    public void setTopmostViewedPostId(Long topmostViewedPostId) {
        this.topmostViewedPostId = topmostViewedPostId;
    }

    public Date getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Date firstLogin) {
        this.firstLogin = firstLogin;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Boolean getNewUser() {
        return newUser;
    }

    public void setNewUser(Boolean newUser) {
        this.newUser = newUser;
    }

    
}
