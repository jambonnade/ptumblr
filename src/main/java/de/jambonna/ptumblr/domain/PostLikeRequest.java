package de.jambonna.ptumblr.domain;

import com.tumblr.jumblr.JumblrClientEx;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Request for "like" action
 */
@Entity
public class PostLikeRequest extends Request {
    @NotNull
    private Long postTumblrId;
    
    @NotNull
    private Boolean unlike;

    
    public Long getPostTumblrId() {
        return postTumblrId;
    }

    public void setPostTumblrId(Long postTumblrId) {
        this.postTumblrId = postTumblrId;
    }

    public Boolean getUnlike() {
        return unlike;
    }

    public void setUnlike(Boolean unlike) {
        this.unlike = unlike;
    }

    
    public void doRequest(JumblrClientEx client, Post post) {
        setPostTumblrId(post.getTumblrId());
        
        beforeRequest();
        if (getUnlike()) {
            client.unlike(getPostTumblrId(), post.getReblogKey());
        } else {
            client.like(getPostTumblrId(), post.getReblogKey());
        }
        afterRequest(client);
    }
}
