package de.jambonna.ptumblr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Entity
public class LinkPost extends Post {
    @Column(length = 1024)
    @NotNull
    private String url;

    private String title;
    
    @Lob
    @NotNull
    private String description;

    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public void setFromApiPost(com.tumblr.jumblr.types.Post apiPost) {
        super.setFromApiPost(apiPost);
        com.tumblr.jumblr.types.LinkPost post = 
            (com.tumblr.jumblr.types.LinkPost)apiPost;

        setUrl(post.getLinkUrl());
        setTitle(post.getTitle());
        setDescription(post.getDescription());
    }

}
