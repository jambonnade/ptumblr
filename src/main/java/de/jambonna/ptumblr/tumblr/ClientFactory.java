package de.jambonna.ptumblr.tumblr;

import com.tumblr.jumblr.JumblrClientEx;
import de.jambonna.ptumblr.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth1.GenericOAuth1ServiceProvider;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Version;
import org.springframework.stereotype.Component;

/**
 * Factory to create a tumblr client
 */
@Component
public class ClientFactory {
    private final String consumerKey;
    private final String consumerSecret;

    
    public ClientFactory(
            @Value("${tumblr.api.consumerKey}") String consumerKey,
            @Value("${tumblr.api.consumerSecret}") String consumerSecret) {
        if (consumerKey == null || consumerKey.length() == 0) {
            throw new IllegalArgumentException("Invalid tumblr api consumer key");
        }
        if (consumerSecret == null || consumerSecret.length() == 0) {
            throw new IllegalArgumentException("Invalid tumblr api consumer secret");
        }
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public JumblrClientEx getNewClient() {
        JumblrClientEx client = new JumblrClientEx(consumerKey, consumerSecret);
        return client;
    }
    
    public JumblrClientEx getNewUserClient(User user) {
        JumblrClientEx client = getNewClient();
        client.setToken(user.getAccessToken(), user.getAccessTokenSecret());
        return client;
    }
    
    public OAuth1Operations getOauthOperations() {
        GenericOAuth1ServiceProvider provider = new GenericOAuth1ServiceProvider(
                consumerKey, consumerSecret, 
                "https://www.tumblr.com/oauth/request_token",
                "https://www.tumblr.com/oauth/authorize",
                null,
                "https://www.tumblr.com/oauth/access_token",
                OAuth1Version.CORE_10_REVISION_A);
        return provider.getOAuthOperations();
    }

}
