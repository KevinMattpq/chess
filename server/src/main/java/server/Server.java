package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import server.service.ResponseException;
import server.service.Service;

public class Server {

    private final Javalin javalin;
    private final Service service = new Service();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", this::clear);
        javalin.post("/user",this::register);
        javalin.post("/session", this::login);
        javalin.delete("/session",this::logout);
        javalin.get("/game", this::listOfGames);
        javalin.post("/game",this::createGame);
        javalin.put("/game",this::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private void clear(Context ctx){
        try{
            service.clearAll();
        }catch (ResponseException e){
            if(e.getMessage() == "Database Error"){
                ctx.status(500);
                ctx.result(e.toJson());
            }
        }

    }

    private  void register(Context ctx) throws ResponseException {
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        try{
            AuthData auth = service.register(userData);
            ctx.result(new Gson().toJson(auth));
            ctx.status(200);
        }catch (ResponseException errorResponse){
            if(errorResponse.getMessage() == "Error: Bad Request"){
                ctx.status(400);
                ctx.result(errorResponse.toJson());
            }else {
                ctx.status(403);
                ctx.result(errorResponse.toJson());
            }
        }

    }

    public void login(Context ctx) throws ResponseException{
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        try{
            AuthData auth = service.login(userData);
            ctx.result(new Gson().toJson(auth));
            ctx.status(200);
        }catch (ResponseException loginError){
            if(loginError.getMessage() == "Error: Bad Request"){
                ctx.status(400);
                ctx.result(loginError.toJson());
            }
            if (loginError.getMessage() == "Error: unauthorized") {
                ctx.status(401);
                ctx.result(loginError.toJson());
            }
        }
    }


    public void  logout(Context ctx) throws ResponseException {
        String authToken = ctx.header("authorization");
        try {
            service.logout(authToken);
        } catch (ResponseException logoutError) {
            if (logoutError.getMessage() == "Error: unauthorized") {
                ctx.status(401);
                ctx.result(logoutError.toJson());
            }
        }
    }


    public void createGame(Context ctx) throws ResponseException{
        GameData userData = new Gson().fromJson(ctx.body(),GameData.class);
        String authToken = ctx.header("authorization");
        try {
            service.isUserLogin(authToken);
            CreateGameResult newGame = service.createGame(userData);
            ctx.result(new Gson().toJson(newGame));
            ctx.status(200);
        }catch (ResponseException createError){
            if(createError.getMessage() == "Error: Bad Request"){
                ctx.status(400);
                ctx.result(createError.toJson());
            }
            if(createError.getMessage() == "Error: Unauthorized"){
                ctx.status(401);
                ctx.result(createError.toJson());
            }
        }
    }

    public void listOfGames(Context ctx) throws ResponseException{
        String authToken = ctx.header("authorization");
        try{
            service.isUserLogin(authToken);
            ListOfGamesResult result = service.listOfGames(authToken);
            ctx.result(new Gson().toJson(result));
            ctx.status(200);
        }catch (ResponseException listOfGamesError){
            if(listOfGamesError.getMessage() == "Error: Unauthorized"){
                ctx.status(401);
                ctx.result(listOfGamesError.toJson());
            }
        }
    }

    public void joinGame(Context ctx) throws ResponseException{
        JoinGameRequest userData = new Gson().fromJson(ctx.body(),JoinGameRequest.class);
        String authToken = ctx.header("authorization");
        try{
            AuthData authData = service.isUserLogin(authToken);
            service.joinGame(userData,authData.username());
        }catch (ResponseException joinGameError){
            if(joinGameError.getMessage() == "Error: Bad Request"){
                ctx.status(400);
                ctx.result(joinGameError.toJson());
            }
            if(joinGameError.getMessage() == "Error: Unauthorized"){
                ctx.status(401);
                ctx.result(joinGameError.toJson());
            }
            if(joinGameError.getMessage() == "Error: AlreadyTaken"){
                ctx.status(403);
                ctx.result(joinGameError.toJson());
            }

        }
    }


    public void stop() {
        javalin.stop();
    }
}
