package de.jambonna.ptumblr.domain;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;


@Entity
public class AnswerPost extends Post {
    @NotNull
    private String askingName;
    
    @Lob
    @NotNull
    private String question;
    
    @Lob
    @NotNull
    private String answer;

    
    public String getAskingName() {
        return askingName;
    }

    public void setAskingName(String askingName) {
        this.askingName = askingName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    
    @Override
    public void setFromApiPost(com.tumblr.jumblr.types.Post apiPost) {
        super.setFromApiPost(apiPost);
        com.tumblr.jumblr.types.AnswerPost post = 
                (com.tumblr.jumblr.types.AnswerPost)apiPost;
        setAskingName(post.getAskingName());
        setQuestion(post.getQuestion());
        setAnswer(post.getAnswer());
    }

}
