package server;

import io.javalin.*;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", this::clear);
        javalin.post("/user",this::register);
//        javalin.post("/session", this::login);
//        javalin.delete("/session",this::logout);
//        javalin.get("/game", this::listOfGames);
//        javalin.post("/game",this::createGame);
//        javalin.put("/game",this::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private void clear(Context ctx){
        System.out.print("Test");
        Handler handler = new Handler();
        handler.handlerClear();
    }

    private  void register(Context ctx){
        System.out.print("Test Register");
        Handler handler = new Handler();
        handler.handlerRegister();
    }

//    public void login(Context ctx){
//        System.out.print("Testing Login");
//        Handler handler = new Handler();
//        handler.handlerLogin();
//    }
    public void stop() {
        javalin.stop();
    }
}
