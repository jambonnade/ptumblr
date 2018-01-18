package de.jambonna.ptumblr.dao;

import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.domain.User;
import java.util.List;

/**
 * Persistence operations relative to User entities and Post entites linked to it
 */
public interface UserDao {
    public User findUserById(Long id);
    public User findUserByUsername(String username);
    public void saveUser(User user);
    public List<Post> getPostsTo(User user, Long postId, Integer nb);
    public List<Post> getPostsAfter(User user, Long postId, Integer nb);
    public void savePosts(List<Post> posts);
    public void clearPosts(User user);
    public Post findPost(Long postId);
}
