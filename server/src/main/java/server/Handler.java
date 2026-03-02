package server;

import server.service.Service;

public class Handler {
    Service service = new Service();

    public String handlerClear(){
        service.clearAll();
        return "[200]{}" ;
    }

    public String handlerRegister(){
        service.register();
        return  "{username :, password :, email : }";
    }

    public String handleLogin(){
        //
    }
}
