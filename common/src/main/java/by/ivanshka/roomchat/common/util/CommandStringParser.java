package by.ivanshka.roomchat.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandStringParser {
    public static List<String> parseInputToCommandAndArgs(String input) {
        char[] chars = input.toCharArray();

        boolean manyWordsArgument = false;
        boolean lastCharIsSpaceOrDoubleQuote = false;

        List<String> myArgs = new ArrayList<>();

        int startArgIndex = 0;

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (ch == '"') {
                manyWordsArgument = !manyWordsArgument;
                if (manyWordsArgument) {
                    startArgIndex = i + 1;
                } else {
                    String arg = input.substring(startArgIndex, i);
                    myArgs.add(arg);
                    startArgIndex = i + 1;
                    lastCharIsSpaceOrDoubleQuote = true;
                }
            } else if (ch == ' ') {
                if (lastCharIsSpaceOrDoubleQuote) {
                    startArgIndex = i + 1;
                    continue;
                }
                if (!manyWordsArgument) {
                    String arg = input.substring(startArgIndex, i);
                    myArgs.add(arg);
                    startArgIndex = i + 1;
                    lastCharIsSpaceOrDoubleQuote = true;
                }
            } else if (i == chars.length - 1) {
                String arg = input.substring(startArgIndex);
                myArgs.add(arg);
            } else {
                lastCharIsSpaceOrDoubleQuote = false;
            }

            if (ch == '\\') {
                i++;
            }
        }

        // parsing some special symbols
        return myArgs.stream()
                .map(arg -> arg.replace("\\\\", "\\")
                        .replace("\\\"", "\"")
                ).collect(Collectors.toList());
    }
}
