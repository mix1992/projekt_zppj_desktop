package radio.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import radio.Main;
import radio.rest.DTO.LoginResponseMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Base64;

public class LoginController {

    @FXML
    public TextField emailInput;

    @FXML
    public PasswordField passwordInput;

    public void login(ActionEvent actionEvent) {
        if (emailInput.getText().isEmpty() || passwordInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nie wypełniłeś danych logowania");
            alert.setHeaderText("Błąd");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
        } else {
            String auth = emailInput.getText().toString() + ":" + passwordInput.getText().toString();
            Call<LoginResponseMessage> call = Main.restService.login(new String(Base64.getEncoder().encode(auth.getBytes())));
            call.enqueue(new Callback<LoginResponseMessage>() {
                @Override
                public void onResponse(Call<LoginResponseMessage> call, Response<LoginResponseMessage> response) {
                    if (response.isSuccessful()) {
                        LoginResponseMessage loginResponseMessage = response.body();
                        Main.setToken(loginResponseMessage.getToken());
                        Main.setUserDTO(loginResponseMessage.getUserDTO());

                        try {
                            Main.primaryStage.getScene().setRoot(FXMLLoader.load(Main.radio));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseMessage> call, Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
            });
        }
    }
}
