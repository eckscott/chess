package ui;

import java.util.Scanner;

public class PreLogin {

    public void run() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "♕ 240 Chess Client: type <help> for options");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();
            result = eval(line);
            if (!result.equals("quit")) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            }
            else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "Thanks for playing!");
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input){
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        return switch (cmd){
            case "quit" -> "quit";
            case "login" -> "Login functionality place holder";
            case "register" -> "register place holder";
            default -> help();
        };
    }

    public String help() {
        return """
                help - lists command options
                quit - exits the program
                login <USERNAME> <PASSWORD> - to play chess
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                """;
    }
}
