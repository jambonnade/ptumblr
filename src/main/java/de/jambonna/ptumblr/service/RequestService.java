package de.jambonna.ptumblr.service;

import de.jambonna.ptumblr.domain.ApiRateLimitException;
import de.jambonna.ptumblr.domain.ApiRatelimit;
import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.domain.User;
import java.util.List;

/**
 *
 */
public interface RequestService {
    public ApiRatelimit getLastRequestApiLimit();
    public ApiRatelimit canRequest();
    public com.tumblr.jumblr.types.User userInfo(User user) throws ApiRateLimitException;
    public List<com.tumblr.jumblr.types.Post> userDashboard(User user, Long sinceId, Integer limit) throws ApiRateLimitException;
    public void likePost(Post post, boolean unlike) throws ApiRateLimitException;
    public void reblogPost(Post post) throws ApiRateLimitException;
}
