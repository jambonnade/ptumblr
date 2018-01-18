package de.jambonna.ptumblr.domain;

import com.tumblr.jumblr.types.Photo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
public class PhotoPost extends Post {
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<PostPhoto> photos = new ArrayList<>();
    
    @Lob
    @NotNull
    private String caption;

    
    public List<PostPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PostPhoto> photos) {
        this.photos = photos;
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
        com.tumblr.jumblr.types.PhotoPost post = 
            (com.tumblr.jumblr.types.PhotoPost)apiPost;

        setCaption(post.getCaption());
        photos.clear();
        for (Photo apiPhoto: post.getPhotos()) {
            PostPhoto photo = new PostPhoto();
            photo.setPost(this);
            photo.setFromApiPhoto(apiPhoto);
            photos.add(photo);
        }
    }

}
