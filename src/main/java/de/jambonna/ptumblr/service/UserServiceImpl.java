package de.jambonna.ptumblr.service;

import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.domain.User;
import de.jambonna.ptumblr.dao.UserDao;
import de.jambonna.ptumblr.domain.ApiRateLimitException;
import de.jambonna.ptumblr.domain.PostPage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RequestService requestService;
    private final Integer pageSize;
    private final Integer dashboardRequestPageSize;

    public UserServiceImpl(UserDao userDao, RequestService requestService, 
            @Value("${app.pageSize}") Integer pageSize,
            @Value("${tumblr.api.dashboardRequestPageSize}") Integer dashboardRequestPageSize) {
        this.userDao = userDao;
        this.requestService = requestService;
        if (pageSize == null || pageSize < 1) {
            throw new IllegalArgumentException("Invalid page size");
        }
        this.pageSize = pageSize;
        if (dashboardRequestPageSize == null || dashboardRequestPageSize < pageSize) {
            throw new IllegalArgumentException("Invalid dashboard request page size");
        }
        this.dashboardRequestPageSize = dashboardRequestPageSize;
    }
    
    @Override
    public User loadById(Long id) {
        return userDao.findUserById(id);
    }

    @Override
    @Transactional
    public User login(String accesToken, String accesTokenSecret) throws ApiRateLimitException {
        User user = new User();
        user.setAccessToken(accesToken);
        user.setAccessTokenSecret(accesTokenSecret);
        com.tumblr.jumblr.types.User userInfo = requestService.userInfo(user);
        User existingUser = userDao.findUserByUsername(userInfo.getName());
        if (existingUser == null) {
            user.setUsername(userInfo.getName());
        } else {
            user = existingUser;
            user.setAccessToken(accesToken);
            user.setAccessTokenSecret(accesTokenSecret);
        }
        Date now = new Date();
        user.setLastLogin(now);
        if (user.getFirstLogin() == null) {
            user.setFirstLogin(now);
        }
        
        user.setNewUser(existingUser != null);
        
        userDao.saveUser(user);

        return user;
    }

    @Override
    @Transactional
    public PostPage getPostPageForViewing(User user, Long topPostTumblrId) {
        PostPage page = new PostPage();
        page.setRequestedTopPostId(topPostTumblrId);
        
        List<Post> postsTo = userDao.getPostsTo(user, topPostTumblrId, pageSize + 1);
        
        Post topPost = null;
        if (postsTo.size() > 0) {
            topPost = postsTo.get(0);
        }
        if (postsTo.size() > pageSize) {
            page.setOlderPageTopPost(postsTo.get(pageSize));
            page.setPosts(postsTo.subList(0, pageSize));
        } else {
            page.setPosts(postsTo);
        }
        
        if (topPost != null) {
            List<Post> postsAfter = userDao.getPostsAfter(user, topPost.getTumblrId(), pageSize);
            if (postsAfter.size() > 0) {
                page.setNewerPageTopPost(postsAfter.get(postsAfter.size() - 1));
            }
        }
        
        user.setLastViewedTopPostId(null);
        if (topPost != null) {
            user.setLastViewedTopPostId(topPost.getTumblrId());
            if (user.getTopmostViewedPostId() == null 
                    || user.getTopmostViewedPostId() < topPost.getTumblrId()) {
                user.setTopmostViewedPostId(topPost.getTumblrId());
            }
        }
        user.setLastAccess(new Date());
        userDao.saveUser(user);
        return page;
    }

    @Override
    @Transactional
    public void resetPosts(User user) {
        user.setTopmostViewedPostId(null);
        user.setLastViewedTopPostId(null);
        userDao.saveUser(user);
        userDao.clearPosts(user);
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public Post getTopmostSavedPost(User user) {
        List<Post> posts = userDao.getPostsTo(user, null, 1);
        return posts.size() > 0 ? posts.get(0) : null;
    }
    
    @Override
    @Transactional    
    public List<Post> fetchNewPosts(User user) throws ApiRateLimitException {
        Post topmostPost = getTopmostSavedPost(user);
        
        List<com.tumblr.jumblr.types.Post> tumblrPosts =
                requestService.userDashboard(user,
                        topmostPost != null ? topmostPost.getTumblrId() : null,
                        dashboardRequestPageSize);
        Date requestDate = new Date();
        List<Post> posts = new ArrayList<>();
        for (com.tumblr.jumblr.types.Post tumblrPost: tumblrPosts) {
            Post p = Post.createFromApiPost(tumblrPost);
            p.setRequestDate(requestDate);
            p.setUser(user);
            posts.add(p);
        }
        userDao.savePosts(posts);
        return posts;
    }

    @Override
    @Transactional
    public void likePost(User user, Long postId) throws ApiRateLimitException {
        Post post = userDao.findPost(postId);
        if (post == null || post.getUser() == null) {
            throw new IllegalArgumentException("Invalid post id: " + postId);
        }
        if (!post.getUser().equals(user)) {
            throw new IllegalArgumentException(
                    "Invalid post user " + post.getUser().getUserId() + " / " + user.getUserId());
        }
        boolean toUnlike = post.getLiked() == true;
        requestService.likePost(post, toUnlike);
        
        // Change like status
        post.setLiked(!toUnlike);
        userDao.savePosts(Arrays.asList(post));
    }

    @Override
    @Transactional
    public void reblogPost(User user, Long postId) throws ApiRateLimitException {
        Post post = userDao.findPost(postId);
        if (post == null || post.getUser() == null) {
            throw new IllegalArgumentException("Invalid post id: " + postId);
        }
        if (!post.getUser().equals(user)) {
            throw new IllegalArgumentException(
                    "Invalid post user " + post.getUser().getUserId() + " / " + user.getUserId());
        }
        requestService.reblogPost(post);
    }

    
}
