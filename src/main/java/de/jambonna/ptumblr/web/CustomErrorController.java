package de.jambonna.ptumblr.web;

import de.jambonna.ptumblr.app.ExceptionLogger;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";
    
    private final ExceptionLogger exceptionLogger;

    
    public CustomErrorController(ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }
    

    @RequestMapping(value = PATH, produces = "text/html; charset=utf-8")
    @ResponseBody
    public String error(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html><body>\n");
        sb.append("<h1>Error ");
        sb.append(request.getAttribute("javax.servlet.error.status_code"));
        sb.append("</h1>\n");

        Object attr = request.getAttribute("javax.servlet.error.exception");
        if (attr != null && attr instanceof Throwable) {
            Throwable exception = (Throwable)attr;
            UUID loggedExcUid = exceptionLogger.log(exception);
            sb.append("<p>Reference: <strong>");
            sb.append(loggedExcUid);
            sb.append("</strong></p>\n");
        }
//        sb.append("attributes:\n<br/><br/>");
//        for (Enumeration<String> e = request.getAttributeNames(); e.hasMoreElements(); ) {
//            String a = e.nextElement();
//            sb.append(a);
//            sb.append("='");
//            sb.append(request.getAttribute(a));
//            sb.append("'<br/><br/>\n\n\n");
//        }
        sb.append("</html></body>");
        return sb.toString();
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
