package server;

import server.service.Service;

public class Handler {

    public String handlerClear(){
        Service service = new Service();
        service.clearAll();
        return "{}" ;
    }
}
