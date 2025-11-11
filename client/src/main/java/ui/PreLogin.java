package ui;

import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PreLogin {

    private final ServerFacade server;

    public PreLogin(int port) {
        server = new ServerFacade(port);
    }

    public void run() throws Exception {
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

    public String eval(String input) throws Exception {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd){
            case "quit" -> "quit";
            case "login" -> "Login functionality place holder";
            case "register" -> register(params);
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

    private String register(String... params) throws Exception {
        if (params.length == 3){
            String username = params[0];
            String password = params[1];
            String email = params[2];
            var auth = server.register(username, password, email);
            return String.format("You have successfully registered and are now signed in as %s", auth.username());
        }
        throw new Exception("Didn't work");
    }
}
