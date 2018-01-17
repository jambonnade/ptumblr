package de.jambonna.ptumblr.domain;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.JumblrClientEx;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Entity
public class PostReblogRequest extends Request {
    @NotNull
    private Long postTumblrId;
    
    @NotNull
    private String blogName;

    
    public Long getPostTumblrId() {
        return postTumblrId;
    }

    public void setPostTumblrId(Long postTumblrId) {
        this.postTumblrId = postTumblrId;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }
    
    

    
    public void doRequest(JumblrClientEx client, Post post) {
        setPostTumblrId(post.getTumblrId());
        setBlogName(post.getUser().getUsername());
        
        beforeRequest();
        // Use a custom method to reblog, since JumblrClient.postReblog() is bugged
        client.reblog(getBlogName(), getPostTumblrId(), post.getReblogKey());
        afterRequest(client);
    }
}
