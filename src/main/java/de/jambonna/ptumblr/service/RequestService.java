package de.jambonna.ptumblr.service;

import de.jambonna.ptumblr.domain.ApiRateLimitException;
import de.jambonna.ptumblr.domain.ApiRatelimit;
import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.domain.User;
import java.util.List;

/**
 * All operations related to tumblr API calls.
 * Operations doing an API call may throw a JumblrException if there is an
 * error during the call, or an ApiRateLimitException if the app judges it can't
 * make the call.
 * These operations persist Request entities of the right type
 */
public interface RequestService {
    /**
     * Returns the API rate limit given by the last API call in the App.
     * 
     * @return a valid ApiRatelimit object or null if no call has been made
     */
    public ApiRatelimit getLastRequestApiLimit();
    
    /**
     * Check if an API call can be made regarding the last known API rate limit.
     * 
     * @return null if can do an API call, or the last ApiRatelimit if cannot.
     */
    public ApiRatelimit canRequest();
    
    /**
     * Requests tumblr user information related to the given App User
     * 
     * @param user a User entity
     * @return a Jumblr User object
     * @throws ApiRateLimitException 
     */
    public com.tumblr.jumblr.types.User userInfo(User user) throws ApiRateLimitException;
    
    /**
     * Retrieves tumblr dashboard posts created after the sinceId post id.
     * Posts are sorted by creation order descending
     * 
     * @param user the User for which we request dashboard posts
     * @param sinceId a tumblr post id from which we request posts (it is excluded)
     *      or null to retrieve posts from the top.
     * @param limit maximum number of posts retrieved
     * @return a Jumblr post List
     * @throws ApiRateLimitException 
     */
    public List<com.tumblr.jumblr.types.Post> userDashboard(User user, Long sinceId, Integer limit) throws ApiRateLimitException;
    
    /**
     * Like or unlike the given Post with the linked User. The linked User
     * is the one who made the dahsboard request
     * 
     * @param post a Post entity coming from a dashboard request
     * @param unlike false to like, true to unlike
     * @throws ApiRateLimitException 
     */
    public void likePost(Post post, boolean unlike) throws ApiRateLimitException;
    
    /**
     * Reblog the given Post with the linked User. The linked User
     * is the one who made the dahsboard request
     * 
     * @param post a Post entity coming from a dashboard request
     * @throws ApiRateLimitException 
     */
    public void reblogPost(Post post) throws ApiRateLimitException;
}
