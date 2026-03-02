package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import server.service.Service;

public class Server {

    private final Javalin javalin;
    private final Service service = new Service();

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
        service.clearAll();
        System.out.print("Test");
    }

    private  void register(Context ctx){
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData auth = service.register(userData);
        ctx.result(new Gson().toJson(auth));
        ctx.status(200);
        System.out.print("Test Register");
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
