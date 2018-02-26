package sportsclub.boot.config.axon;

import org.axonframework.eventhandling.ErrorContext;

public class ErrorHandler implements org.axonframework.eventhandling.ErrorHandler {
    @Override
    public void handleError(ErrorContext errorContext) throws Exception {
        System.out.println("error handling");
    }

}
