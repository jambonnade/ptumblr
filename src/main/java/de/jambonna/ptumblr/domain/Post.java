package de.jambonna.ptumblr.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * A post item from a user dashboard.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(indexes = { @Index(columnList = "userId,tumblrId", unique = true) })
public abstract class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
        
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date requestDate;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    
    @NotNull
    private Long tumblrId;
    
    @NotNull 
    private String blogName;
    
    @NotNull
    private String postUrl;
    
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date postDate;
        
    private String reblogKey;
    
    private Long rebloggedFromId;
    
    private String rebloggedFromName;
    
    private Boolean liked;
    

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTumblrId() {
        return tumblrId;
    }

    public void setTumblrId(Long tumblrId) {
        this.tumblrId = tumblrId;
    }


    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }
    
    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getReblogKey() {
        return reblogKey;
    }

    public void setReblogKey(String reblogKey) {
        this.reblogKey = reblogKey;
    }

    public Long getRebloggedFromId() {
        return rebloggedFromId;
    }

    public void setRebloggedFromId(Long rebloggedFromId) {
        this.rebloggedFromId = rebloggedFromId;
    }

    public String getRebloggedFromName() {
        return rebloggedFromName;
    }

    public void setRebloggedFromName(String rebloggedFromName) {
        this.rebloggedFromName = rebloggedFromName;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
    
    public void setFromApiPost(com.tumblr.jumblr.types.Post apiPost) {
        setTumblrId(apiPost.getId());
        setBlogName(apiPost.getBlogName());
        setPostUrl(apiPost.getShortUrl());
        setPostDate(new Date(apiPost.getTimestamp() * 1000));
        setReblogKey(apiPost.getReblogKey());
        setRebloggedFromId(apiPost.getRebloggedFromId());
        setRebloggedFromName(apiPost.getRebloggedFromName());
        setLiked(apiPost.isLiked());
    }
    
    public static Post createFromApiPost(com.tumblr.jumblr.types.Post apiPost) {
        Post p = createType(apiPost.getType());
        p.setFromApiPost(apiPost);
        return p;
    }
    
    public static Post createType(String tumblrPostType) {
        switch (tumblrPostType) {
            case "text":
                return new TextPost();
            case "photo":
                return new PhotoPost();
            case "quote":
                return new QuotePost();
            case "link":
                return new LinkPost();
            case "chat":
                return new ChatPost();
            case "audio":
                return new AudioPost();
            case "video":
                return new VideoPost();
            case "answer":
                return new AnswerPost();
            default:
                return new UnknownPost();
        }
    }
}
