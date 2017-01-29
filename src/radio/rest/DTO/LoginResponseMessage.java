package radio.rest.DTO;

/**
 * Created by bartek on 28.01.17.
 */
public class LoginResponseMessage {

    private UserDTO userDTO;
    private String token;
    private String tokenExpirationTime;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(String tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }
}
