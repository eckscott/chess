package ui;

import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PreLogin {

    private final ServerFacade server;
    private States currState;

    public PreLogin(int port) {
        server = new ServerFacade(port);
        currState = States.SIGNEDOUT;
    }

    public States run() throws Exception {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "♕ 240 Chess Client: type <help> for options");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while ((!result.equals("quit")) && (currState == States.SIGNEDOUT)){
            printPrompt();
            String line = scanner.nextLine();
            result = eval(line);
            if (result.equals("quit")){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "Thanks for playing!");
                currState = States.QUIT;
                return currState;
            }
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
        }
        return currState;
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
            case "login" -> login(params);
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
            currState = States.SIGNEDIN;
            return String.format("You have successfully registered and are now signed in as %s\n", auth.username());
        }
        throw new Exception("Didn't work");
    }

    private String login(String... params) throws Exception{
        if (params.length == 2){
            String username = params[0];
            String password = params[1];
            var auth = server.login(username, password);
            currState = States.SIGNEDIN;
            return String.format("You have successfully signed in as %s\n", auth.username());
        }
        throw new Exception("Invalid input: expected <USERNAME> <PASSWORD>");
    }
}
