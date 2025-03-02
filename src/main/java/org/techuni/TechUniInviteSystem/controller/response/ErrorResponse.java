package org.techuni.TechUniInviteSystem.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

@Value
public class ErrorResponse {

    @JsonIgnore
    HttpStatus httpStatus;
    ZonedDateTime timestamp;
    int status;
    String error;
    String message;

    public ErrorResponse(HttpStatus status, String message, ZoneId zoneId) {
        this.httpStatus = status;
        this.timestamp = ZonedDateTime.now(zoneId);
        this.status = status.value();
        this.error = status.name();
        this.message = Optional.ofNullable(message).orElse(status.getReasonPhrase());
    }

    public ErrorResponse(HttpStatus status, ZoneId zoneId) {
        this(status, null, zoneId);
    }

    public ModelAndView createView() {
        ModelAndView mav = new ModelAndView();
        mav.setStatus(this.httpStatus);
        mav.addObject("errorCode", this.status);
        mav.addObject("errorType", this.error);
        mav.addObject("message", this.message);
        mav.addObject("timestamp", this.timestamp.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return mav;
    }
}
