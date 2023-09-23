package by.ivanshka.roomchat.client.ui;

import by.ivanshka.roomchat.client.chat.ChatController;
import by.ivanshka.roomchat.client.exception.AlreadyJoinedRoomException;
import by.ivanshka.roomchat.client.exception.InvalidSessionParameterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainController {
    private final ChatController chatController;

    public void start() {

        chatController.connect();

        Scanner scanner = new Scanner(System.in);

        String input;

        while (true) {
            try {
                input = scanner.nextLine();

                if (input.equals("/exit")) {
                    break;
                }

                handleUserInput(input);

            } catch (Exception e) {
                handleException(e);
            }
        }

        chatController.disconnect();
    }

    private void handleException(Exception e) {
        if (e instanceof InvalidSessionParameterException) {
            log.warn("Failed. " + e.getMessage());
        } else if (e instanceof AlreadyJoinedRoomException) {
            log.warn("Failed. You have to leave current room before joining new.");
        } else {
            log.error("Exception when chatting", e);
        }
    }

    private void handleUserInput(String input) {
        //todo refactor using command pattern

        if (input.startsWith("/setusername")) {
            String username = input.split(" ", 2)[1];
            chatController.setUsername(username);

        } else if (input.startsWith("/joinroom")) {
            String roomId = input.split(" ", 2)[1];
            chatController.joinRoom(roomId);

        } else if (input.equals("/leaveroom")) {
            chatController.leaveRoom();

        } else {
            chatController.sendMessage(input);
        }
    }
}
