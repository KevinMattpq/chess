package client;

import model.UserData;
import server.service.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    public void run(){
        System.out.println("♕ Welcome to Chess. Sign in to start.");
        System.out.print(help());

        //Reading the input
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            //Processing part
            try{
                //Saving the result of eval
                result = eval(line);
                //Printing the result in the console
                System.out.print(result);
            }catch(Throwable e){
                //Handleing the error
                var msg = e.toString();
                System.out.print(msg);
            }
        }


    }

    public String eval(String input) {
        try {
            //Converts everythin into lowercase and grabs each value separated by a space
            String[] tokens = input.toLowerCase().split(" ");
            //Grabing the command or help
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            //Getting parameter after the fist word
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login","l" -> login(params);
                case "register","r" -> register(params);
                case "quit","q" -> quit();
                case "help","h" -> help();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }



    public String login(String... params) throws ResponseException {
        if(params.length == 2){
            String username = params[0];
            String password = params[1];
        }
     return null;
    }

    public String register(String... params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        String email = params[2];
        UserData newUser = new UserData(username,password,email);
        //result = server.regiter(newUser);


        return null;
    }
    public String quit(String... params) throws ResponseException {
        return null;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }

    //It will let the user know where to type
    private void printPrompt() {
        System.out.print("\n>>> ");
    }
}
