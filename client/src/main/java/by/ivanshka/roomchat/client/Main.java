package by.ivanshka.roomchat.client;

import by.ivanshka.roomchat.client.chat.ChatController;
import by.ivanshka.roomchat.client.exception.AlreadyJoinedRoomException;
import by.ivanshka.roomchat.client.exception.InvalidSessionParameterException;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class Main {
    private static final ChatController CHAT_CONTROLLER = new ChatController("localhost", 8080, log::info);

    public static void main(String[] args) {

        CHAT_CONTROLLER.connect();

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

        CHAT_CONTROLLER.disconnect();
    }

    private static void handleException(Exception e) {
        if (e instanceof InvalidSessionParameterException) {
            log.warn("Failed. " + e.getMessage());
        } else if (e instanceof AlreadyJoinedRoomException) {
            log.warn("Failed. You have to leave current room before joining new.");
        } else {
            log.error("Exception when chatting", e);
        }
    }

    private static void handleUserInput(String input) {
        //todo refactor using command pattern

        if (input.startsWith("/setusername")) {
            String username = input.split(" ", 2)[1];
            CHAT_CONTROLLER.setUsername(username);

        } else if (input.startsWith("/joinroom")) {
            String roomId = input.split(" ", 2)[1];
            CHAT_CONTROLLER.joinRoom(roomId);

        } else if (input.equals("/leaveroom")) {
            CHAT_CONTROLLER.leaveRoom();

        } else {
            CHAT_CONTROLLER.sendMessage(input);
        }
    }
}