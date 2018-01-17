package de.jambonna.ptumblr.web;

import de.jambonna.ptumblr.app.ExceptionLogger;
import de.jambonna.ptumblr.app.Urls;
import de.jambonna.ptumblr.domain.ApiRateLimitException;
import de.jambonna.ptumblr.domain.ApiRatelimit;
import de.jambonna.ptumblr.domain.Post;
import de.jambonna.ptumblr.domain.PostPage;
import de.jambonna.ptumblr.domain.User;
import de.jambonna.ptumblr.service.RequestService;
import de.jambonna.ptumblr.service.UserService;
import de.jambonna.ptumblr.tumblr.ClientFactory;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 */
@Controller
@SessionAttributes("userSession")
public class MainController {
    private final UserService userService;
    private final RequestService requestService;
    private final ClientFactory clientFactory;
    private final Urls urls;
    private final ExceptionLogger exceptionLogger;
    private final Integer pageSize;


    public MainController(UserService userService, 
            ClientFactory clientFactory, RequestService requestService,
            ExceptionLogger exceptionLogger, Urls urls,
            @Value("${app.pageSize}") Integer pageSize) {
        this.userService = userService;
        this.requestService = requestService;
        this.clientFactory = clientFactory;
        this.exceptionLogger = exceptionLogger;
        this.urls = urls;
        this.pageSize = pageSize;
    }

    @ModelAttribute
    public UserSession getUserSession() {
        return new UserSession();
    }
    
    @ModelAttribute
    public User getUser(@ModelAttribute UserSession s) throws ApiRateLimitException {
        User user = new User();
        Long userId = s.getUserId();
        if (userId != null) {
            User loadedUser = userService.loadById(userId);
            if (loadedUser == null) {
                s.setUserId(null);
            } else {
                user = loadedUser;
            }
        } else {
            OAuthToken at = s.getTumblrAccessToken();
            if (at != null) {
                user = userService.login(at.getValue(), at.getSecret());
                s.setUserId(user.getUserId());
                s.setTumblrAccessToken(null);
                s.setJustLoggedIn(true);
                s.setJustLoggedBack(user.getNewUser());
            }
        }
        return user;
    }
    
    @ModelAttribute
    public void addApiRatelimit(Model model) {
        ApiRatelimit arl = requestService.canRequest();
        Boolean canRequest = Boolean.FALSE;
        if (arl == null) {
            arl = requestService.getLastRequestApiLimit();
            canRequest = Boolean.TRUE;
        }
        model.addAttribute("canUseApi", canRequest);
        model.addAttribute("apiRatelimit", arl);
    }
    
    @GetMapping("/")
    public String homeAction() {
        return "home";
    }
    
    @GetMapping("/api")
    public String apiInfoAction() {
        return "apiinfo";
    }

    @GetMapping("/dashboard")
    public String dashboardAction(Model model, @ModelAttribute User user) {
        String result = checkAccessAndRedirect(user);
        if (result != null) {
            return result;
        }

        Long topPostId = user.getLastViewedTopPostId();
        if (topPostId == null) {
            Post topPost = userService.getTopmostSavedPost(user);
            if (topPost != null) {
                topPostId = topPost.getTumblrId();
            }
        }
        if (topPostId != null) {
            return "redirect:" + urls.getDashboardUrl(topPostId);
        }
        
        PostPage page = new PostPage();
        model.addAttribute(page);

        return "dashboard";
    }
    
    @RequestMapping("/dashboard/reset")
    public String dashboardResetAction(@ModelAttribute User user) throws ApiRateLimitException {
        String result = checkAccessAndRedirect(user);
        if (result != null) {
            return result;
        }

        userService.resetPosts(user);
        List<Post> posts = userService.fetchNewPosts(user);
        return "redirect:" + urls.getDashboardUrl();
    }
    
    @RequestMapping("/dashboard/fetchnew")
    public String dashboardFetchNewAction(@ModelAttribute User user)
            throws ApiRateLimitException {
        String result = checkAccessAndRedirect(user);
        if (result != null) {
            return result;
        }

        List<Post> posts = userService.fetchNewPosts(user);
        if (posts.size() > 0) {
            Post topPost = posts.get(Math.max(posts.size() - pageSize, 0));
            return "redirect:" + urls.getDashboardUrl(topPost.getTumblrId());
        }
        
        return "redirect:" + urls.getDashboardUrl();
    }
    
    @GetMapping("/dashboard/to/{tumblrId}")
    public String dashboardToIdAction(Model model,
                @PathVariable Long tumblrId, @ModelAttribute User user) {
        String result = checkAccessAndRedirect(user);
        if (result != null) {
            return result;
        }
        
        PostPage page = userService.getPostPageForViewing(user, tumblrId);
        model.addAttribute(page);
        return "dashboard";
    }
    
    @RequestMapping("/disconnect")
    public String disconnectAction(@ModelAttribute UserSession s) {
        s.setUserId(null);
        return "redirect:" + urls.getBaseUrl();
    }

    @RequestMapping("/post/{postId}/like")
    public String likePostAction(@PathVariable Long postId, 
                @ModelAttribute User user, @ModelAttribute UserSession s) 
            throws ApiRateLimitException {
        String result = checkAccessAndRedirect(user);
        if (result != null) {
            return result;
        }

        userService.likePost(user, postId);
        s.setJustLikedPostId(postId);
        return "redirect:" + urls.getDashboardUrl();
    }

    @RequestMapping("/post/{postId}/reblog")
    public String reblogPostAction(@PathVariable Long postId, 
                @ModelAttribute User user, @ModelAttribute UserSession s) 
            throws ApiRateLimitException {
        String result = checkAccessAndRedirect(user);
        if (result != null) {
            return result;
        }

        userService.reblogPost(user, postId);
        s.setJustRebloggedPostId(postId);
        return "redirect:" + urls.getDashboardUrl();
    }

    
    @GetMapping("/connectid")
    public String connectIdAction(@ModelAttribute UserSession s, @RequestParam("id") Long userId) {
        s.setUserId(userId);
        s.setJustLoggedIn(true);
        s.setJustLoggedBack(true);
        return "redirect:" + urls.getDashboardUrl();
    }
        
    
    @GetMapping("/oauthconnect")
    public String oauthConnectAction(@ModelAttribute User user, @ModelAttribute UserSession s) {
        if (user.hasAccesToken()) {
            return "redirect:" + urls.getDashboardUrl();
        }
        OAuth1Operations oauthOperations = clientFactory.getOauthOperations();
        String callbackUrl = urls.getOAuthVerifyUrl();
        OAuthToken requestToken = oauthOperations.fetchRequestToken(callbackUrl, null);
        s.setTumblrRequestToken(requestToken);
        String authorizeUrl = oauthOperations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE);
        return "redirect:" + authorizeUrl;
    }
    
    
    @GetMapping("/oauthverify")
    public String oauthVerifyAction(@ModelAttribute UserSession s,
            @RequestParam("oauth_token") String tokenParam, 
            @RequestParam("oauth_verifier") String verifierParam) {
        OAuthToken requestToken = s.getTumblrRequestToken();
        if (requestToken == null || !requestToken.getValue().equals(tokenParam)) {
            return "redirect:" + urls.getOAuthConnectUrl();
        }
        
        OAuth1Operations oauthOperations = clientFactory.getOauthOperations();
        OAuthToken accessToken = oauthOperations.exchangeForAccessToken(
                new AuthorizedRequestToken(requestToken, verifierParam), null);
        // Just save the acces token in session, do the actual user lookup / update
        // in another page to not risk failures here
        s.setTumblrAccessToken(accessToken);
        s.setTumblrRequestToken(null);
        
        return "redirect:" + urls.getDashboardUrl();
    }
    


    @ExceptionHandler(Throwable.class)
    public String exceptionHandler(Model model, Throwable exception) {
        UUID loggedExcUid = exceptionLogger.log(exception);
        model.addAttribute("exceptionId", loggedExcUid);
        model.addAttribute("exception", exception);
        return "exception";
    }


    
    protected String checkAccessAndRedirect(User user) {
        if (!user.hasAccesToken()) {
            return "redirect:" + urls.getOAuthConnectUrl();
        }
        return null;
    }
}
