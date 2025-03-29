package org.example.medicalrecordproject.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListeners {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEventListeners.class);

    @EventListener
    public void handleSuccessEvent(AuthenticationSuccessEvent event) {
        logger.info("Authentication success for user: {}", event.getAuthentication().getName());
    }

    @EventListener
    public void handleFailureEvent(AbstractAuthenticationFailureEvent event) {
        logger.warn("Authentication failure for user: {}", event.getAuthentication().getName());
    }
}
