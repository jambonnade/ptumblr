package de.jambonna.ptumblr.domain;

import com.tumblr.jumblr.types.Video;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
public class VideoPost extends Post {
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PostVideo> players = new ArrayList<>();
    
    @Lob
    private String caption;

    public List<PostVideo> getPlayers() {
        return players;
    }

    public void setPlayers(List<PostVideo> players) {
        this.players = players;
    }

    public PostVideo getLargestPlayer(Integer maxWidth) {
        PostVideo largest = null;
        for (PostVideo v: players) {
            if (largest == null || largest.getWidth() < v.getWidth()) {
                if (maxWidth == null || v.getWidth() <= maxWidth) {
                    largest = v;
                }
            }
        }
        if (largest == null) {
            throw new IllegalStateException("Unable to get largest player");
        }
        return largest;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    @Override
    public void setFromApiPost(com.tumblr.jumblr.types.Post apiPost) {
        super.setFromApiPost(apiPost);
        com.tumblr.jumblr.types.VideoPost post = 
            (com.tumblr.jumblr.types.VideoPost)apiPost;

        setCaption(post.getCaption());
        players.clear();
        for (Video apiVideo : post.getVideos()) {
            PostVideo video = new PostVideo();
            video.setPost(this);
            video.setFromApiVideo(apiVideo);
            players.add(video);
        }
    }

}
