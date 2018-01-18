package de.jambonna.ptumblr.domain;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;


@Entity
public class QuotePost extends Post {
    @NotNull
    @Lob
    private String text;
    
    @NotNull
    @Lob
    private String source;

    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public void setFromApiPost(com.tumblr.jumblr.types.Post apiPost) {
        super.setFromApiPost(apiPost);
        com.tumblr.jumblr.types.QuotePost post = 
            (com.tumblr.jumblr.types.QuotePost)apiPost;

        setText(post.getText());
        setSource(post.getSource());
    }

}
