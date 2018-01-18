package de.jambonna.ptumblr.service;

import de.jambonna.ptumblr.domain.ApiRateLimitException;
import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.domain.PostPage;
import de.jambonna.ptumblr.domain.User;
import java.util.List;

/**
 * Operations related to an app User.
 * Some operations make API calls so they may throw a 
 * JumblrException or an ApiRateLimitException
 */
public interface UserService {
    /**
     * Loads a User entity by the given id
     * 
     * @param id the User id
     * @return the User entity or null if not found
     */
    public User loadById(Long id);
    
    /**
     * Login operation after receiving the user's tumblr access token. 
     * It creates or updates a User entity with the given tumblr token.
     * It makes an API call to get the tumblr user name
     * 
     * @param accesToken access token
     * @param accesTokenSecret access token screen
     * @return a persited User entity linked to the given access token
     * @throws ApiRateLimitException 
     */
    public User login(String accesToken, String accesTokenSecret) throws ApiRateLimitException;
    
    /**
     * Gets a page of saved Post entities with topPostTumblrId as most recent post.
     * Items are ordered in post date descending
     * 
     * @param user the User for which we get dashboard posts
     * @param topPostTumblrId tumblr id of the most recent post to be returned
     * @return a PostPage object with Post entities and data needed for navigation
     */
    public PostPage getPostPageForViewing(User user, Long topPostTumblrId);
    
    /**
     * Resets all saved Post entities for the given app User
     * 
     * @param user the User entity
     */
    public void resetPosts(User user);
    
    /**
     * Gets the most recent saved Post entity fetched for this user
     * 
     * @param user the User entity
     * @return the Post entity or null if no saved Post
     */
    public Post getTopmostSavedPost(User user);
    
    /**
     * Requests new dashboard posts for this user. Makes a dashboard API call.
     * Fetches posts after the most recent saved post.
     * 
     * @param user the User entity
     * @return a list of saved Post enities
     * @throws ApiRateLimitException 
     */
    public List<Post> fetchNewPosts(User user) throws ApiRateLimitException;
    
    /**
     * Like/unlike the given dashboard post with this user.
     * The post has to be a dashboard saved post fetched for this user.
     * 
     * @param user the User entity
     * @param postId a Post entity id (not a tumblr id)
     * @throws ApiRateLimitException 
     */
    public void likePost(User user, Long postId) throws ApiRateLimitException;
    
    /**
     * Reblog the given dashboard post with this user.
     * The post has to be a dashboard saved post fetched for this user.
     * 
     * @param user the User entity
     * @param postId a Post entity id (not a tumblr id)
     * @throws ApiRateLimitException 
     */
    public void reblogPost(User user, Long postId) throws ApiRateLimitException;
}
