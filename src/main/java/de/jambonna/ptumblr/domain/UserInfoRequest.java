package de.jambonna.ptumblr.domain;

import com.tumblr.jumblr.JumblrClientEx;
import javax.persistence.Entity;

/**
 * Request to get information on the linked User
 */
@Entity
public class UserInfoRequest extends Request {
    public com.tumblr.jumblr.types.User doRequest(JumblrClientEx client) {
        beforeRequest();
        com.tumblr.jumblr.types.User user = client.user();
        afterRequest(client);
        return user;
    }
}
