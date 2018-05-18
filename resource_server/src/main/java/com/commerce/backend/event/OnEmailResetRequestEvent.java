package com.commerce.backend.event;


import com.commerce.backend.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnEmailResetRequestEvent extends ApplicationEvent {

    private User user;
    private String appUrl;
    private String newEmail;

    public OnEmailResetRequestEvent(User user, String appUrl, String newEmail) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.newEmail = newEmail;
    }
}
