package de.jambonna.ptumblr.service;

import de.jambonna.ptumblr.domain.Request;
import de.jambonna.ptumblr.domain.UserInfoRequest;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class RequestServiceImplTest {
    

    @Test
    public void testSetLastRequest() {
        Long ts = new Date().getTime();
        RequestServiceImpl rs = new RequestServiceImpl(null, null, 0);
        
        try {
            rs.setLastRequest(createRequestWithRateLimit(null, 0, ts, ts));
            fail("No exception thrown");
        } catch (IllegalArgumentException ex) {
        }
        try {
            rs.setLastRequest(createRequestWithRateLimit(0, null, ts, ts));
            fail("No exception thrown");
        } catch (IllegalArgumentException ex) {
        }
        try {
            rs.setLastRequest(createRequestWithRateLimit(0, 0, null, ts));
            fail("No exception thrown");
        } catch (IllegalArgumentException ex) {
        }
        try {
            rs.setLastRequest(createRequestWithRateLimit(0, 0, ts, null));
            fail("No exception thrown");
        } catch (IllegalArgumentException ex) {
        }

        rs.setLastRequest(createRequestWithRateLimit(0, 0, ts, ts));
    }
    
    @Test
    public void testCanRequest() {
        Long past = new Date().getTime();
        Long future = past + 3600 * 1000;
        RequestServiceImpl rs = new RequestServiceImpl(null, null, 50);
        
        // Can request if no last request
        assertNull(rs.canRequest());
        
        // Can request if not under limits
        rs.setLastRequest(createRequestWithRateLimit(51, 51, future, future));
        assertNull(rs.canRequest());
        rs.setLastRequest(createRequestWithRateLimit(50, 50, future, future));
        assertNull(rs.canRequest());
        
        // Can request if under limits and reset date reached
        rs.setLastRequest(createRequestWithRateLimit(49, 49, past, past));
        assertNull(rs.canRequest());
        rs.setLastRequest(createRequestWithRateLimit(49, 50, past, future));
        assertNull(rs.canRequest());
        rs.setLastRequest(createRequestWithRateLimit(50, 49, future, past));
        assertNull(rs.canRequest());

        // Cannot request if under limits and reset date not reached
        rs.setLastRequest(createRequestWithRateLimit(49, 49, future, future));
        assertNotNull(rs.canRequest());
        rs.setLastRequest(createRequestWithRateLimit(49, 49, future, past));
        assertNotNull(rs.canRequest());
        rs.setLastRequest(createRequestWithRateLimit(49, 49, past, future));
        assertNotNull(rs.canRequest());
        rs.setLastRequest(createRequestWithRateLimit(49, 50, future, future));
        assertNotNull(rs.canRequest());
        rs.setLastRequest(createRequestWithRateLimit(49, 50, future, past));
        assertNotNull(rs.canRequest());
        rs.setLastRequest(createRequestWithRateLimit(50, 49, future, future));
        assertNotNull(rs.canRequest());
        rs.setLastRequest(createRequestWithRateLimit(50, 49, past, future));
        assertNotNull(rs.canRequest());
        
    }
    
    public Request createRequestWithRateLimit(
            Integer perHourRemaining, Integer perDayRemaining, 
            Long perHourResetDate, Long perDayResetDate) {
        Request r = new UserInfoRequest();
        r.getRateLimit().setPerHourRemaining(perHourRemaining);
        r.getRateLimit().setPerDayRemaining(perDayRemaining);
        r.getRateLimit().setPerHourResetTime(perHourResetDate);
        r.getRateLimit().setPerDayResetTime(perDayResetDate);
        return r;
    }
    
}
