package de.jambonna.ptumblr.domain;

import com.tumblr.jumblr.types.Video;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@Entity
public class PostVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postVideoId;
    
    @ManyToOne
    @NotNull
    private Post post;
    
    @NotNull
    private Integer width;
    
    @Lob
    @NotNull
    private String embedCode;

    public Long getPostVideoId() {
        return postVideoId;
    }

    public void setPostVideoId(Long postVideoId) {
        this.postVideoId = postVideoId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getEmbedCode() {
        return embedCode;
    }

    public void setEmbedCode(String embedCode) {
        this.embedCode = embedCode;
    }

    public void setFromApiVideo(Video video) {
        setWidth(video.getWidth());
        setEmbedCode(video.getEmbedCode());
    }
    
    public String getAlteredEmbedCode() {
        Document doc = Jsoup.parse(getEmbedCode());
        Elements children = doc.body().children();
        if (children.size() == 1) {
            Element video = children.first();
            if ("video".equals(video.tagName())) {
                video.removeAttr("muted");
                video.attr("controls", true);
                
                return video.outerHtml();
            }
        }
        return getEmbedCode();
    }
}
