package it.polimi.ingsw.network.message;

public class LoginRequest extends Message {
    private String username;

    public LoginRequest(String username) {
        super(username, MessageType.LOGIN_REQUEST);
        this.username = username;
    }


    public String getUsername() {
        return username;
    }
}
