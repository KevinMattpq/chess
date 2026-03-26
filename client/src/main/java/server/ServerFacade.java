package server;

import com.google.gson.Gson;
import model.*;
import server.service.ResponseException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public AuthData userInfo;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    private HttpRequest buildRequest(String method, String path, Object body,String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null){
            request.setHeader("authorization",authToken);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException("Error");
        }
    }

    //This function will return wether it was successful or not
    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new ResponseException("Error");
            }

            throw new ResponseException("Error Handle");
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }



    //SIGNEDOUT METHODS
    public AuthData login(LoginRequest userInfo) throws ResponseException {
        var request = buildRequest("POST",	"/session",userInfo,null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public UserData register(UserData userData) throws ResponseException {
        var request = buildRequest("POST", "/user",userData,null);
        var response = sendRequest(request);
        return handleResponse(response, UserData.class);
    }



    //SIGNEDIN METHODS
    public ListOfGamesResult listOfGames(String authToken) throws ResponseException {
        var request = buildRequest("GET","/game",null,authToken);
        var respose = sendRequest(request);
        return handleResponse(respose, ListOfGamesResult.class);
    }

    public CreateGameResult createGame(GameData gameData,String authToken) throws ResponseException{
        var request = buildRequest("POST","/game",gameData,authToken);
        var response = sendRequest(request);
        return handleResponse(response,CreateGameResult.class);
    }

//    public void playGame(String color, String gameId, String authToken){
//        var request = buildRequest("PUT","/game",color,gameId,authToken);
//    }

//    public void logout(String authToken) throws ResponseException {
//        var request = buildRequest("POST",	"/session",null,authToken);
//        var response = sendRequest(request);
//        return handleResponse(response,);
//    }


//    public void clear(){
//        var request = buildRequest("DELETE", "/db");
//        var response = sendRequest(request);
//        return handleResponse(response);
//    }





}
