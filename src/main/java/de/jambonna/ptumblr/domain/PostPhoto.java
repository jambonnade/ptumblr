package de.jambonna.ptumblr.domain;

import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoSize;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;


@Entity
public class PostPhoto {
    public static int WEB_SIZE = 540;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postPhotoId;
    
    @ManyToOne
    @NotNull
    private Post post;
    
    @NotNull
    private Integer origWidth;
    @NotNull
    private Integer origHeight;
    
    @Column(length = 1024)
    @NotNull
    private String origUrl;

    private Integer webHeight;
    
    @Column(length = 1024)
    private String webUrl;

    
    public Long getPostPhotoId() {
        return postPhotoId;
    }

    public void setPostPhotoId(Long postPhotoId) {
        this.postPhotoId = postPhotoId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Integer getOrigWidth() {
        return origWidth;
    }

    public void setOrigWidth(Integer origWidth) {
        this.origWidth = origWidth;
    }

    public Integer getOrigHeight() {
        return origHeight;
    }

    public void setOrigHeight(Integer origHeight) {
        this.origHeight = origHeight;
    }

    public String getOrigUrl() {
        return origUrl;
    }

    public void setOrigUrl(String origUrl) {
        this.origUrl = origUrl;
    }

    public Integer getWebHeight() {
        return webHeight;
    }

    public void setWebHeight(Integer webHeight) {
        this.webHeight = webHeight;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
    
    
    
    
    public void setFromApiPhoto(Photo photo) {
        PhotoSize s = photo.getOriginalSize();
        setOrigUrl(s.getUrl());
        setOrigWidth(s.getWidth());
        setOrigHeight(s.getHeight());
        for (PhotoSize ps: photo.getSizes()) {
            if (ps.getWidth() == WEB_SIZE) {
                setWebHeight(ps.getHeight());
                setWebUrl(ps.getUrl());
                break;
            }
        }
    }
}
