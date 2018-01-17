package de.jambonna.ptumblr.service;

import com.tumblr.jumblr.JumblrClientEx;
import de.jambonna.ptumblr.domain.Request;
import de.jambonna.ptumblr.dao.RequestDao;
import de.jambonna.ptumblr.domain.ApiRateLimitException;
import de.jambonna.ptumblr.domain.User;
import de.jambonna.ptumblr.domain.UserDashboardRequest;
import de.jambonna.ptumblr.domain.UserInfoRequest;
import de.jambonna.ptumblr.domain.ApiRatelimit;
import de.jambonna.ptumblr.domain.PostLikeRequest;
import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.domain.PostReblogRequest;
import de.jambonna.ptumblr.tumblr.ClientFactory;
import java.rmi.server.UID;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Service
public class RequestServiceImpl implements RequestService {

    private final static Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
    
    private final ClientFactory clientFactory;
    private final RequestDao requestDao;
    private final Integer rateLimitMargin;
    private ApiRatelimit lastRequestApiLimit;
    
    
    public RequestServiceImpl(
            ClientFactory clientFactory, RequestDao requestDao, 
            @Value("${tumblr.api.rateLimitMargin}") Integer rateLimitMargin) {
        this.clientFactory = clientFactory;
        this.requestDao = requestDao;
        if (rateLimitMargin == null) {
            throw new IllegalArgumentException("No rate limit margin");
        }
        this.rateLimitMargin = rateLimitMargin;
    }
    
    @PostConstruct
    public void init() {
        Request lastRequest = requestDao.findLastRequest();
        if (lastRequest != null) {
            setLastRequest(lastRequest);
        }
    }

    public void setLastRequest(Request lastRequest) {
        ApiRatelimit arl = lastRequest != null ? 
                new ApiRatelimit(lastRequest.getRateLimit()) : 
                new ApiRatelimit();
        if (!arl.isValid()) {
            throw new IllegalArgumentException("Invalid last request api rate limit");
        }
        this.lastRequestApiLimit = arl;
    }

    @Override
    public ApiRatelimit getLastRequestApiLimit() {
        return lastRequestApiLimit;
    }
    
    @Override
    public ApiRatelimit canRequest() {
        ApiRatelimit arl = lastRequestApiLimit;
        boolean result = true;
        UUID logId = UUID.randomUUID();

        if (arl == null) {
            logger.debug("canRequest {} - no last request", logId);
        } else {
            Date now = new Date();
            DateFormat df = DateFormat.getDateTimeInstance(
                    DateFormat.FULL, DateFormat.FULL, Locale.ROOT);


            if (result && arl.getPerDayRemaining() < rateLimitMargin) {
                logger.debug(
                        "canRequest {} - limit per day reached ({} < {})",
                        logId, arl.getPerDayRemaining(), rateLimitMargin);

                if (now.getTime() > arl.getPerDayResetTime()) {
                    logger.debug(
                            "canRequest {} - reset time reached ({})", 
                            logId, df.format(arl.getPerDayResetDate()));
                } else {
                    logger.debug(
                            "canRequest {} - reset time not reached ({})", 
                            logId, df.format(arl.getPerDayResetDate()));
                    result = false;
                }
            }
            if (result && arl.getPerHourRemaining() < rateLimitMargin) {
                logger.debug(
                        "canRequest {} - limit per hour reached ({} < {})",
                        logId, arl.getPerHourRemaining(), rateLimitMargin);

                if (now.getTime() > arl.getPerHourResetTime()) {
                    logger.debug(
                            "canRequest {} - reset time reached ({})", 
                            logId, 
                            df.format(arl.getPerHourResetDate()));
                } else {
                    logger.debug(
                            "canRequest {} - reset time not reached ({})", 
                            logId, 
                            df.format(arl.getPerHourResetDate()));
                    result = false;
                }
            }
        }
        logger.debug("canRequest {} - result : {}", logId, result ? "yes" : "no");
        return result ? null : arl;
    }

    @Override
    @Transactional
    public com.tumblr.jumblr.types.User userInfo(User user) throws ApiRateLimitException {
        UUID logId = UUID.randomUUID();
        logger.debug("userInfo {} - start", logId);

        
        checkCanRequest();
        
        UserInfoRequest request = new UserInfoRequest();
        if (user.getUserId() != null) {
            request.setUser(user);
        }
        logger.info("userInfo {} - doing api call (user {} / {})...", 
                logId, user.getUserId(), user.getUsername());
        JumblrClientEx client = clientFactory.getNewUserClient(user);
        logger.debug("userInfo {} - client : {}, {}", logId, client.getClass().getName(), client.getRequestBuilder().getClass().getName());
        com.tumblr.jumblr.types.User userInfo = request.doRequest(client);
        setLastRequest(request);
        requestDao.saveRequest(request);
        logger.debug("userInfo {} - end (result username : {})", logId, userInfo.getName());

        return userInfo;
    }

    @Override
    @Transactional
    public List<com.tumblr.jumblr.types.Post> userDashboard(User user, Long sinceId, Integer limit)
            throws ApiRateLimitException {
        UUID logId = UUID.randomUUID();
        logger.debug("userDashboard {} - start", logId);

        checkCanRequest();
        
        UserDashboardRequest request = new UserDashboardRequest();
        request.setUser(user);
        request.setSinceId(sinceId);
        request.setPostLimit(limit);
        logger.info("userDashboard {} - doing api call (user {} / {}, since id {}, limit {})...", 
                logId, user.getUserId(), user.getUsername(), sinceId, limit);
        JumblrClientEx client = clientFactory.getNewUserClient(user);
        List<com.tumblr.jumblr.types.Post> posts = request.doRequest(client);
        logger.debug("userDashboard {} - body : {}", logId, client.getLastResponseBody());
        setLastRequest(request);
        requestDao.saveRequest(request);
        
        logger.debug("userDashboard {} - end (posts : {})", logId, posts.size());

        return posts;
    }

    protected void checkCanRequest() throws ApiRateLimitException {
        ApiRatelimit arl = canRequest();
        if (arl != null) {
            throw new ApiRateLimitException(arl);
        }
    }

    @Transactional
    @Override
    public void likePost(Post post, boolean unlike) throws ApiRateLimitException {
        UUID logId = UUID.randomUUID();
        logger.debug("likePost {} - start", logId);

        User user = post.getUser();
        if (user == null || post.getTumblrId() == null) {
            throw new IllegalArgumentException("Invalid post");
        }
        
        checkCanRequest();
        
        PostLikeRequest request = new PostLikeRequest();
        request.setUser(user);
        request.setUnlike(unlike);
        logger.info("likePost {} - doing api call (user {} / {}, id {} / {}, unlike {})...", 
                logId, user.getUserId(), user.getUsername(), 
                post.getPostId(), post.getTumblrId(), 
                unlike);
        JumblrClientEx client = clientFactory.getNewUserClient(user);
        request.doRequest(client, post);
        logger.debug("likePost {} - body : {}", logId, client.getLastResponseBody());
        setLastRequest(request);
        requestDao.saveRequest(request);
        
        logger.debug("likePost {} - end", logId);
    }

    @Transactional    
    @Override
    public void reblogPost(Post post) throws ApiRateLimitException {
        UUID logId = UUID.randomUUID();
        logger.debug("reblogPost {} - start", logId);

        User user = post.getUser();
        if (user == null || post.getTumblrId() == null) {
            throw new IllegalArgumentException("Invalid post");
        }
        
        checkCanRequest();
        
        PostReblogRequest request = new PostReblogRequest();
        request.setUser(user);
        logger.info("reblogPost {} - doing api call (user {} / {}, id {} / {})...", 
                logId, user.getUserId(), user.getUsername(), 
                post.getPostId(), post.getTumblrId());
        JumblrClientEx client = clientFactory.getNewUserClient(user);
        request.doRequest(client, post);
        logger.debug("reblogPost {} - body : {}", logId, client.getLastResponseBody());
        setLastRequest(request);
        requestDao.saveRequest(request);
        
        logger.debug("reblogPost {} - end", logId);
    }
}
