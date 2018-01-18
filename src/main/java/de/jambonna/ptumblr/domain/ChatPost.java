package de.jambonna.ptumblr.domain;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;


@Entity
public class ChatPost extends Post {
    private String title;
    
    @Lob
    @NotNull
    private String body;

    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    @Override
    public void setFromApiPost(com.tumblr.jumblr.types.Post apiPost) {
        super.setFromApiPost(apiPost);
        com.tumblr.jumblr.types.ChatPost post = 
                (com.tumblr.jumblr.types.ChatPost)apiPost;
        
        setTitle(post.getTitle());
        setBody(post.getBody());
    }
}
