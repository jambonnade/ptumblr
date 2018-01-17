package de.jambonna.ptumblr.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 */
@Component
public class UrlBuilder {
    private final String baseUrl;
//    private final Object[] nullArg = { null };
    

    public UrlBuilder(@Value("${app.baseUrl}") String baseUrl) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("Invalid baseUrl");
        }
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
        
    
//    public UriComponentsBuilder fromMethodName(Class<?> controllerType, String methodName, Object... args) {
//        return fromController(controllerType).withM
//        if (baseUrl.length() != 0) {
//            UriComponentsBuilder baseUrlB = UriComponentsBuilder.fromHttpUrl(baseUrl);
//            return MvcUriComponentsBuilder.fromMethodName(baseUrlB, controllerType, methodName, args);
//        } else {
//            return MvcUriComponentsBuilder.fromMethodName(controllerType, methodName, args);
//        }        
//    }
//    
//    public UriComponentsBuilder fromMethodNameNullArg(Class<?> controllerType, String methodName) {
//        return fromMethodName(controllerType, methodName, nullArg);
//    }
    
    public UriComponentsBuilder fromController(Class<?> controllerType) {
        if (baseUrl.length() != 0) {
            UriComponentsBuilder baseUrlB = UriComponentsBuilder.fromHttpUrl(baseUrl);
            return MvcUriComponentsBuilder.fromController(baseUrlB, controllerType);
        } else {
            return MvcUriComponentsBuilder.fromController(controllerType);
        }        
    }

}
