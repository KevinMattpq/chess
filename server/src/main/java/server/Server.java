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
    public void stop() {
        javalin.stop();
    }
}
