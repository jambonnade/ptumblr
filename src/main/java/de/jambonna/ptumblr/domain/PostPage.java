package de.jambonna.ptumblr.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PostPage {
    private Long requestedTopPostId;
    private List<Post> posts = new ArrayList<>();
    private Post olderPageTopPost;
    private Post newerPageTopPost;

    
    public Long getRequestedTopPostId() {
        return requestedTopPostId;
    }

    public void setRequestedTopPostId(Long requestedTopPostId) {
        this.requestedTopPostId = requestedTopPostId;
    }    
    
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    
    public Long getTopPostId() {
        if (posts != null && posts.size() > 0) {
            return posts.get(0).getTumblrId();
        }
        return null;
    }

    public Post getOlderPageTopPost() {
        return olderPageTopPost;
    }

    public void setOlderPageTopPost(Post olderPageTopPost) {
        this.olderPageTopPost = olderPageTopPost;
    }

    public Post getNewerPageTopPost() {
        return newerPageTopPost;
    }

    public void setNewerPageTopPost(Post newerPageTopPost) {
        this.newerPageTopPost = newerPageTopPost;
    }
    
    
}
