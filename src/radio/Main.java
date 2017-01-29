package radio;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import radio.rest.DTO.UserDTO;
import radio.rest.RestService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sun.misc.GC;

import java.net.URL;

public class Main extends Application {

    static public RestService restService;
    static public UserDTO userDTO;
    static public String token;
    static public URL login;
    static public URL radio;
    static public Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        login = getClass().getResource("views/login.fxml");
        radio = getClass().getResource("views/radio.fxml");
        primaryStage.setTitle("DesktopRadio");
        primaryStage.setScene(new Scene(FXMLLoader.load(login), 600, 400));
        primaryStage.show();

        this.primaryStage = primaryStage;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getApiAddress())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restService = retrofit.create(RestService.class);

    }

    public static void main(String[] args) {
        launch(args);
    }

    public String getApiAddress() {
        return "http://localhost:8080/Radio/rest/";
    }

    public static RestService getRestService() {
        return restService;
    }

    public static void setRestService(RestService restService) {
        Main.restService = restService;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Main.token = token;
    }

    public static UserDTO getUserDTO() {
        return userDTO;
    }

    public static void setUserDTO(UserDTO userDTO) {
        Main.userDTO = userDTO;
    }

}
