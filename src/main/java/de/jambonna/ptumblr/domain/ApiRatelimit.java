package de.jambonna.ptumblr.domain;

import java.util.Date;
import java.util.Map;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Holds API rate limit information given by API response headers
 */
@Embeddable
public class ApiRatelimit {
    @NotNull
    private Integer perHourRemaining;
    
    @NotNull
    private Integer perDayRemaining;
    
    @NotNull
    private Long perHourResetTime;
    
    @NotNull
    private Long perDayResetTime;

    
    
    public ApiRatelimit() {
    }

    public ApiRatelimit(ApiRatelimit arl) {
        if (arl == null) {
            throw new IllegalArgumentException("Invalid input ApiRatelimit");
        }
        perDayRemaining = arl.getPerDayRemaining();
        perDayResetTime = arl.getPerDayResetTime();
        perHourRemaining = arl.getPerHourRemaining();
        perHourResetTime = arl.getPerHourResetTime();
    }
    
    
    
    public Integer getPerHourRemaining() {
        return perHourRemaining;
    }

    public void setPerHourRemaining(Integer perHourRemaining) {
        this.perHourRemaining = perHourRemaining;
    }

    public Long getPerHourResetTime() {
        return perHourResetTime;
    }

    public void setPerHourResetTime(Long perHourResetTime) {
        this.perHourResetTime = perHourResetTime;
    }
    
    public Date getPerHourResetDate() {
        return new Date(getPerHourResetTime());
    }
    
    public boolean isPerHourResetPassed() {
        Long nowTime = new Date().getTime() / 1000L;
        return perDayResetTime != null && perDayResetTime < nowTime;
    }


    public Integer getPerDayRemaining() {
        return perDayRemaining;
    }

    public void setPerDayRemaining(Integer perDayRemaining) {
        this.perDayRemaining = perDayRemaining;
    }

    public Long getPerDayResetTime() {
        return perDayResetTime;
    }
    
    public Date getPerDayResetDate() {
        return new Date(getPerDayResetTime());
    }

    public void setPerDayResetTime(Long perDayResetTime) {
        this.perDayResetTime = perDayResetTime;
    }
    
    public boolean isPerDayResetPassed() {
        Long nowTime = new Date().getTime() / 1000L;
        return perDayResetTime != null && perDayResetTime < nowTime;
    }

    public void setFromResponseHeaders(Map<String, String> headers) {
        String header;
        
        header = headers.get("X-Ratelimit-Perhour-Remaining");
        setPerHourRemaining(getRemainingValueFromHeader(header));
        header = headers.get("X-Ratelimit-Perday-Remaining");
        setPerDayRemaining(getRemainingValueFromHeader(header));
        header = headers.get("X-Ratelimit-Perhour-Reset");
        setPerHourResetTime(getResetTimeFromHeader(header));
        header = headers.get("X-Ratelimit-Perday-Reset");
        setPerDayResetTime(getResetTimeFromHeader(header));
    }
    
    private Integer getRemainingValueFromHeader(String header) {
        if (header != null) {
            try {
                return Integer.valueOf(header);
            } catch (NumberFormatException ex) {
            }
        }
        return null;
    }
    private Long getResetTimeFromHeader(String header) {
        if (header != null) {
            try {
                int resetSeconds = Integer.valueOf(header);
                Date now = new Date();
                return now.getTime() + resetSeconds * 1000;
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }
    
    public boolean isValid() {
        return getPerDayRemaining() != null
                && getPerDayResetTime() != null
                && getPerHourRemaining() != null
                && getPerHourResetTime() != null;
    }
}
