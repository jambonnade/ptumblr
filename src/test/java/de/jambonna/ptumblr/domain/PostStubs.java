package de.jambonna.ptumblr.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tumblr.jumblr.responses.PostDeserializer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 *
 */
public class PostStubs {
    private static final String[] stubFileNames = {
        "answerpost01.json",
        "audiopost01.json",
        "chatpost01.json",
        "linkpost01.json",
        "photopost01.json",
        "quotepost01.json",
        "textpost01.json",
        "unknownpost01.json",
        "videopost01.json",
    };
    
    private Random random;
    private final Gson gson;

    public PostStubs() {
        gson = new GsonBuilder().registerTypeAdapter(
                    com.tumblr.jumblr.types.Post.class, new PostDeserializer())
                .create();
    }

    public void setRandom(Random random) {
        this.random = random;
    }    
    
    public Random getRandomGenerator() {
        return random;
    }
    
    public InputStream getJsonPostStubStream(String filename) throws IOException {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("stubs/posts/" + filename);
        if (is == null) {
            throw new IOException("Invalid resource " + filename);
        }
        return is;
    }
    
    public String getJsonPostStubContent(String filename) throws IOException {
        InputStream ins = getJsonPostStubStream(filename);
        InputStreamReader insr = new InputStreamReader(ins, "UTF-8");
        BufferedReader br = new BufferedReader(insr);
        StringBuilder sb = new StringBuilder();
        
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public com.tumblr.jumblr.types.Post getApiPostStub(String filename) {
        try {
            String json = getJsonPostStubContent(filename);
            return gson.fromJson(json, com.tumblr.jumblr.types.Post.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public com.tumblr.jumblr.types.Post getRandomApiPostStub() {
        return getApiPostStub(stubFileNames[random.nextInt(stubFileNames.length)]);
    }

    public com.tumblr.jumblr.types.Post getRandomApiPostStub(long tumblrId) {
        com.tumblr.jumblr.types.Post p = getRandomApiPostStub();
        p.setId(tumblrId);
        return p;
    }

}
