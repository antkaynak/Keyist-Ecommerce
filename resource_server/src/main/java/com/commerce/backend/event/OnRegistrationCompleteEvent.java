package com.commerce.backend.event;


import com.commerce.backend.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {


    private User user;
    private String appUrl;

    public OnRegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

}