package de.jambonna.ptumblr.domain;

import com.tumblr.jumblr.JumblrClientEx;
import java.util.Date;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Base request entity. Holds information about an API call
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date createdAt;
    
    @ManyToOne
    private User user;    
    
    @NotNull
    @Embedded
    private ApiRatelimit rateLimit;
    

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ApiRatelimit getRateLimit() {
        if (rateLimit == null) {
            rateLimit = new ApiRatelimit();
        }
        return rateLimit;
    }

    protected void beforeRequest() {
        setCreatedAt(new Date());
    }
    
    protected void afterRequest(JumblrClientEx client) {
        if (client.getLastResponseHeaders() != null) {
            getRateLimit().setFromResponseHeaders(client.getLastResponseHeaders());
        }
    }
    
}
