package ui;

import client.ClientContext;
import model.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PostLogin {

    private final ServerFacade server;
    private States currState;
    private final ClientContext ctx;

    public PostLogin(int port, ClientContext ctx) {
        server = new ServerFacade(port);
        currState = States.SIGNEDIN;
        this.ctx = ctx;
    }

    public States run() throws Exception {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while ((!result.equals("quit")) && (currState == States.SIGNEDIN)){
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
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + "[LOGGED IN] >>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) throws Exception {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd){
            case "quit" -> "quit";
            case "logout" -> logout(params);
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

    private String logout(String... params) throws Exception {
        if (params.length != 0){
            throw new Exception("Provided parameters and expected none");
        }
        server.logout(ctx.getCurrUser());
        return "Thanks for playing!\n";
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
