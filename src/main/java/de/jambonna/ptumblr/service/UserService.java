package de.jambonna.ptumblr.service;

import de.jambonna.ptumblr.domain.ApiRateLimitException;
import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.domain.PostPage;
import de.jambonna.ptumblr.domain.User;
import java.util.List;

/**
 *
 */
public interface UserService {
    public User loadById(Long id);
    public User login(String accesToken, String accesTokenSecret) throws ApiRateLimitException;
    public PostPage getPostPageForViewing(User user, Long topPostTumblrId);
    public void resetPosts(User user);
    public Post getTopmostSavedPost(User user);
    public List<Post> fetchNewPosts(User user) throws ApiRateLimitException;
    public void likePost(User user, Long postId) throws ApiRateLimitException;
    public void reblogPost(User user, Long postId) throws ApiRateLimitException;
}
