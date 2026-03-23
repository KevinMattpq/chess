package client;

import model.UserData;
import server.Server;
import server.ServerFacade;
import server.service.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) throws ResponseException{
        ServerFacade server = new ServerFacade(serverUrl);
    }

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
        if (!(params.length == 2)) {
            throw new ResponseException("Username and Password are required");
        }
        String username = params[0];
        String password = params[1];
        state = State.SIGNEDIN;
        //Calling function from service PENDING

        return "Welcome you are logged in!";
    }

    public String register(String... params) throws ResponseException {
        if (!(params.length == 3)){
            throw new ResponseException("Username, password and email are required");
        }
        String username = params[0];
        String password = params[1];
        String email = params[2];
        UserData newUser = new UserData(username,password,email);

        //Calling function from server PENDING
        //serverFacade.regiter(newUser);
        return "Successfully registered.";
    }
    public String quit(String... params) throws ResponseException {
        if (!(params.length == 1)){
            throw new ResponseException("Error");
        }
        String authToken = params[0];
        state = State.SIGNEDOUT;
        //Calling function from service
        return null;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    Options:
                    Login as an existing user: "l", "login" <USERNAME> <PASSWORD>
                    Register a new user: "r", "register" <USERNAME> <PASSWORD> <EMAIL>
                    Exit the program: "q", "quit"
                    Print this message: "h", "help"
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
