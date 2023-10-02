package by.ivanshka.roomchat.common.exception.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractExceptionHandler implements ExceptionHandler {
    public void printExceptionMessage(Throwable e) {
        log.warn("Error. " + e.getMessage());
    }}
