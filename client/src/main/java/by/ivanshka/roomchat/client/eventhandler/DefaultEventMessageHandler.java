package by.ivanshka.roomchat.client.eventhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class DefaultEventMessageHandler implements Consumer<String> {
    @Override
    public void accept(String s) {
        log.info(s);
    }
}
