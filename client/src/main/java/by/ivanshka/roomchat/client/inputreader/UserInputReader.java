package by.ivanshka.roomchat.client.inputreader;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserInputReader {
    private static final Scanner scanner = new Scanner(System.in);
    public static int readIntegerFromScanner(String invitation, String errorMessage) {
        System.out.print(invitation);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println(errorMessage);
            return readIntegerFromScanner(invitation, errorMessage);
        }
    }

    public static boolean getYesOrNoFromScanner() {
        char firstChar = scanner.nextLine().charAt(0);
        return firstChar == 'y' || firstChar == 'Y';
    }
}
