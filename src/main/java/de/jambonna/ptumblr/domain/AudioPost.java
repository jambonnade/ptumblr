package de.jambonna.ptumblr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Entity
public class AudioPost extends Post {
    @Lob
    @NotNull
    private String caption;
    
    @Lob
    @NotNull
    private String embedCode;

    
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getEmbedCode() {
        return embedCode;
    }

    public void setEmbedCode(String embedCode) {
        this.embedCode = embedCode;
    }

    @Override
    public void setFromApiPost(com.tumblr.jumblr.types.Post apiPost) {
        super.setFromApiPost(apiPost);
        com.tumblr.jumblr.types.AudioPost post = 
                (com.tumblr.jumblr.types.AudioPost)apiPost;
        setCaption(post.getCaption());
        setEmbedCode(post.getEmbedCode());
    }
    
}
