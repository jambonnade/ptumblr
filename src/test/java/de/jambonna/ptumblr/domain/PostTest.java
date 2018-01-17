package de.jambonna.ptumblr.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tumblr.jumblr.responses.PostDeserializer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gruik
 */
public class PostTest {
    
    public PostTest() {
    }


    @Test
    public void testCreateFromApiPost() throws IOException {
        com.tumblr.jumblr.types.Post apiPost;
        
        apiPost = getApiPostStub("textpost01.json");
        TextPost tp = (TextPost)Post.createFromApiPost(apiPost);
        assertEquals("citriccomics", tp.getBlogName());
        assertNull(tp.getLiked());
        assertEquals(1309652239000L, tp.getPostDate().getTime());
        assertNull(tp.getPostId());
        assertEquals("https://tmblr.co/ZMaAUy6hZLZi", tp.getPostUrl());
        assertEquals("7owQPYEn", tp.getReblogKey());
        assertEquals(Long.valueOf(7172992626L), tp.getRebloggedFromId());
        assertEquals("kiadiary", tp.getRebloggedFromName());
        assertNull(tp.getRequestDate());
        assertEquals(Long.valueOf(7173134572L), tp.getTumblrId());
        assertNull(tp.getUser());
        assertEquals("milky rabbit blood?", tp.getTitle());
        assertEquals("<p><a href=\"http://kiakakes.tumblr.com/post/7172992626\" class=\"tumblr_blog\" target=\"_blank\">kiakakes</a>:</p>\n\n<blockquote><p><img src=\"https://68.media.tumblr.com/tumblr_lnqdqtRwaq1qbbuwr.png\"/></p>\n<p>i couldn’t think of something to draw for myself, so i decided to draw for someone else again. this is fan art for <a href=\"http://citriccomics.tumblr.com/\" target=\"_blank\">ian</a>. i love his comics and the way he draw mice. ; u ; um i tried to incorporate elements from his other comics but i failed. but i put in a part of a panel from milky dog in the box thing on the left. = u =</p></blockquote>\n\n\n!!!!!!!!! SO AWESOME!!!! SHEEEESH <a href=\"http://kiakakes.tumblr.com\" target=\"_blank\">Kiakakes</a> can make even the bloodiest skull adorable!!\nAHHHHH Thank you!!", tp.getBody());
        
        apiPost = getApiPostStub("photopost01.json");
        PhotoPost pp = (PhotoPost)Post.createFromApiPost(apiPost);
        assertEquals("derekg", pp.getBlogName());
        assertEquals(Long.valueOf(7431599279L), pp.getTumblrId());
        assertEquals(1, pp.getPhotos().size());
        PostPhoto photo = pp.getPhotos().get(0);
        assertEquals("https://68.media.tumblr.com/tumblr_lo36wbWqqq1qanqwwo1_540.jpg", photo.getWebUrl());
        assertEquals(Integer.valueOf(304), photo.getWebHeight());

        apiPost = getApiPostStub("quotepost01.json");
        QuotePost qp = (QuotePost)Post.createFromApiPost(apiPost);
        assertEquals("museumsandstuff", qp.getBlogName());
        assertEquals(Long.valueOf(126980698495L), qp.getTumblrId());
        assertEquals("The third category is the unlucky ones. After taking a museum-studies course they struggle to get any work at all. They take on more and more voluntary work, perhaps eventually getting a short-term contract post in, for example, documentation. They often end up in jobs they don’t really want because they’re the only ones available. When their first post comes to an end it takes them months to find anything else, and when they do it is often at a similar level. This continues for years, until they eventually are forced to give up on museum work and try a different sector, either because no more jobs are available, or because they have reached perhaps 30 years old, have not progressed and can see no prospect of earning a living wage in museums. In the worst cases, people effectively waste up to 10 years of their life failing to get a reasonable museum job and can become extremely upset, as shown by some of the comments recorded in appendix 2.", qp.getText());
        assertEquals("<p>“<a href=\"http://www.museumsassociation.org/download?id=13718\" target=\"_blank\">The Tomorrow People: Entry to the museum workforce</a>&ldquo; - Maurice Davies, 2007 (via <a href=\"http://www.museumsandstuff.org/\" class=\"tumblr_blog\" target=\"_blank\">museumsandstuff</a>)</p><p>This remains very true. </p>", qp.getSource());

        apiPost = getApiPostStub("linkpost01.json");
        LinkPost lp = (LinkPost)Post.createFromApiPost(apiPost);
        assertEquals("travellingcameraclub", lp.getBlogName());
        assertEquals(Long.valueOf(688472164L), lp.getTumblrId());
        assertEquals("Esther Aarts Illustration | News and Blog: Faq: How do you make those marker doodles?", lp.getTitle());
        assertEquals("http://blog.estadiezijn.nl/post/459075181/faq-how-do-you-make-those-marker-doodles", lp.getUrl());
        assertEquals("<blockquote>\n<p>On my Ipad, of course!</p><p>Nothing better than the latest technology to get the job done. Look at all my apps!</p> <p><img height=\"555\" width=\"500\" src=\"http://farm5.static.flickr.com/4006/4445161463_31da0327c2_o.jpg\" alt=\"my iPad\"/></p><p>My favourite markers are an Edding 400, a Sharpie and a Copic Ciao. The white gouache is from Dr Martins and does a decent job covering up whatever needs to be covered up, and flows.</p></blockquote>", lp.getDescription());
        
        apiPost = getApiPostStub("chatpost01.json");
        ChatPost cp = (ChatPost)Post.createFromApiPost(apiPost);
        assertEquals("david", cp.getBlogName());
        assertEquals(Long.valueOf(158720631860L), cp.getTumblrId());
        assertNull(cp.getTitle());
        assertEquals("Me: I like that band, Europe.\nAli: Can you name one Europe song that isn't Final Countdown?\nMe: ...", cp.getBody());
        
        apiPost = getApiPostStub("audiopost01.json");
        AudioPost ap = (AudioPost)Post.createFromApiPost(apiPost);
        assertEquals("derekg", ap.getBlogName());
        assertEquals(Long.valueOf(5578378101L), ap.getTumblrId());
        assertEquals("<p>Otis Redding never fails me. </p>", ap.getCaption());
        assertEquals("<iframe src=\"https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F899921&amp;visual=true&amp;liking=false&amp;sharing=false&amp;auto_play=false&amp;show_comments=false&amp;continuous_play=false&amp;origin=tumblr\" frameborder=\"0\" allowtransparency=\"true\" class=\"soundcloud_audio_player\" width=\"500\" height=\"500\"></iframe>", ap.getEmbedCode());

        apiPost = getApiPostStub("videopost01.json");
        VideoPost vp = (VideoPost)Post.createFromApiPost(apiPost);
        assertEquals("john", vp.getBlogName());
        assertEquals(Long.valueOf(6522991678L), vp.getTumblrId());
        assertEquals("<p><a href=\"http://foreverneilyoung.tumblr.com/post/6522738445\" target=\"_blank\">foreverneilyoung</a>:</p>\n<blockquote>\n<p><a href=\"http://watchmojo.tumblr.com/post/6521201320\" target=\"_blank\">watchmojo</a>:</p>\n<blockquote>\n<p>Neil Young’s live album “A Treasure” is available today. To celebrate, we take a look at the life and career of the Canadian singer-songwriter.</p>\n</blockquote>\n<p>Neil 101 for you new fans out there.</p>\n</blockquote>\n<p><strong>If you don't know/appreciate Neil Young's impressive body of work, this will help</strong></p>", vp.getCaption());
        assertEquals(3, vp.getPlayers().size());
        assertEquals("<object width=\"400\" height=\"251\"><param name=\"movie\" value=\"http://www.youtube.com/v/4Q1aI7xPo0Y&rel=0&egm=0&showinfo=0&fs=1\"></param><param name=\"wmode\" value=\"transparent\"></param><param name=\"allowFullScreen\" value=\"true\"></param><embed src=\"http://www.youtube.com/v/4Q1aI7xPo0Y&rel=0&egm=0&showinfo=0&fs=1\" type=\"application/x-shockwave-flash\" width=\"400\" height=\"251\" allowFullScreen=\"true\" wmode=\"transparent\"></embed></object>", vp.getPlayers().get(1).getEmbedCode());
        assertEquals(Integer.valueOf(400), vp.getPlayers().get(1).getWidth());
        
        apiPost = getApiPostStub("answerpost01.json");
        AnswerPost anp = (AnswerPost)Post.createFromApiPost(apiPost);
        assertEquals("david", anp.getBlogName());
        assertEquals(Long.valueOf(7504154594L), anp.getTumblrId());
        assertEquals("resonatingly-blog", anp.getAskingName());
        assertEquals("I thought Tumblr started in 2007, yet you have posts from 2006?", anp.getQuestion());
        assertEquals("<p>Good catch! Tumblr <strong>launched</strong> in February 2007. We were testing it for a few months before then.</p>\n<p><strong>Tumblr Trivia:</strong> Before Tumblr, my blog (davidslog.com) was a manually edited, single page, HTML tumblelog.</p>", anp.getAnswer());

        apiPost = getApiPostStub("unknownpost01.json");
        UnknownPost up = (UnknownPost)Post.createFromApiPost(apiPost);
        assertEquals("travellingcameraclub", up.getBlogName());
        assertEquals(Long.valueOf(688472164L), up.getTumblrId());
    }
    
    @Test
    public void testVideoPostEmbedCodeAlteration() throws IOException {
        com.tumblr.jumblr.types.Post apiPost = getApiPostStub("videopost01.json");
        VideoPost vp = (VideoPost)Post.createFromApiPost(apiPost);
        assertEquals("<object width=\"400\" height=\"251\"><param name=\"movie\" value=\"http://www.youtube.com/v/4Q1aI7xPo0Y&rel=0&egm=0&showinfo=0&fs=1\"></param><param name=\"wmode\" value=\"transparent\"></param><param name=\"allowFullScreen\" value=\"true\"></param><embed src=\"http://www.youtube.com/v/4Q1aI7xPo0Y&rel=0&egm=0&showinfo=0&fs=1\" type=\"application/x-shockwave-flash\" width=\"400\" height=\"251\" allowFullScreen=\"true\" wmode=\"transparent\"></embed></object>", vp.getPlayers().get(1).getAlteredEmbedCode());
        
        apiPost = getApiPostStub("videopost02.json");
        vp = (VideoPost)Post.createFromApiPost(apiPost);
        String embedCode = vp.getPlayers().get(1).getAlteredEmbedCode();
        System.out.println(embedCode);
        assertEquals("<video id=\"embed-59810510757cc385763249\" class=\"crt-video crt-skin-default\" width=\"400\" height=\"225\" poster=\"https://68.media.tumblr.com/tumblr_o4qcikcAfv1tmlmwi_frame1.jpg\" preload=\"none\" data-crt-video data-crt-options=\"{&quot;autoheight&quot;:null,&quot;duration&quot;:29,&quot;hdUrl&quot;:&quot;https:\\/\\/oceaniccunt.tumblr.com\\/video_file\\/t:ur6AugUPuATZx3o8_uPMrw\\/163686624475\\/tumblr_o4qcikcAfv1tmlmwi&quot;,&quot;filmstrip&quot;:{&quot;url&quot;:&quot;https:\\/\\/38.media.tumblr.com\\/previews\\/tumblr_o4qcikcAfv1tmlmwi_filmstrip.jpg&quot;,&quot;width&quot;:&quot;200&quot;,&quot;height&quot;:&quot;112&quot;}}\" controls> \n <source src=\"https://oceaniccunt.tumblr.com/video_file/t:ur6AugUPuATZx3o8_uPMrw/163686624475/tumblr_o4qcikcAfv1tmlmwi/480\" type=\"video/mp4\"> \n</video>", embedCode);
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
    
    public com.tumblr.jumblr.types.Post getApiPostStub(String filename) 
            throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(
                    com.tumblr.jumblr.types.Post.class, new PostDeserializer())
                .create();
        String json = getJsonPostStubContent(filename);
        return gson.fromJson(json, com.tumblr.jumblr.types.Post.class);
    }

}
