package de.jambonna.ptumblr.domain;

import com.tumblr.jumblr.JumblrClientEx;
import com.tumblr.jumblr.types.Post;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;

/**
 * Request for fetching new posts in the dashboard
 */
@Entity
public class UserDashboardRequest extends Request {
    private Long sinceId;
    
    private Integer postLimit;

    public Long getSinceId() {
        return sinceId;
    }

    public void setSinceId(Long sinceId) {
        this.sinceId = sinceId;
    }

    public Integer getPostLimit() {
        return postLimit;
    }

    public void setPostLimit(Integer postLimit) {
        this.postLimit = postLimit;
    }
    
    public List<Post> doRequest(JumblrClientEx client) {
        Map<String, String> options = new HashMap<>();
        if (getSinceId() != null) {
            options.put("since_id", getSinceId().toString());
        }
        if (getPostLimit() != null) {
            options.put("limit", getPostLimit().toString());
        }
        options.put("reblog_info", "true");
        beforeRequest();
        List<Post> posts = client.userDashboard(options);
        afterRequest(client);
        return posts;
    }

}
