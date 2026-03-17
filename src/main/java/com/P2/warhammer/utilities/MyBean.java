package com.P2.warhammer.utilities;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MyBean {

    @EventListener
    public void logSessionInits(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(
                sessionInitEvent -> {
                    LoggerFactory.getLogger(getClass()).info("A new Session has been initialized!");
                });


    }

}
