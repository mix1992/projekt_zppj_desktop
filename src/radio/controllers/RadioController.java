package radio.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import okhttp3.ResponseBody;
import radio.Main;
import radio.rest.DTO.StationDTO;
import radio.rest.DTO.StationPathDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * Created by bartek on 28.01.17.
 */
public class RadioController {

    @FXML
    public TextField addressInput;
    @FXML
    public TableView<StationDTO> table;
    @FXML
    public TextField nameInput;
    @FXML
    public TableColumn nameColumn;
    @FXML
    public TableColumn addressColumn;
    @FXML
    public Label currentPlayedPath;

    ObservableList<StationDTO> stations = FXCollections.observableArrayList();

    public RadioController() {
        stations.addListener(new ListChangeListener<StationDTO>() {
            @Override
            public void onChanged(Change<? extends StationDTO> c) {
                table.setItems(stations);
            }
        });
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<StationDTO, String>("name"));
        addressColumn.setCellValueFactory(
                new PropertyValueFactory<StationDTO, String>("path"));
        Call<List<StationDTO>> call = Main.getRestService().getStations(Main.getToken());
        call.enqueue(new Callback<List<StationDTO>>() {
            @Override
            public void onResponse(Call<List<StationDTO>> call, Response<List<StationDTO>> response) {
                if (response.isSuccessful()) {
                    stations.addAll(response.body());
                    table.getSelectionModel().select(0);
                    table.getFocusModel().focus(0);
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<StationDTO>> call, Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });

        table.setRowFactory(tv -> {
            TableRow<StationDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    StationDTO rowData = row.getItem();
                    Call<ResponseBody> callStart = Main.getRestService().startRadio(Main.getToken(), new StationPathDTO(rowData.getPath()));
                    callStart.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                            } else {
                                System.out.println(response.errorBody());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                            System.out.println(throwable.getMessage());
                        }
                    });
                }
            });
            return row;
        });

        refreshCurrentPlayedStationLabel();
    }

    public void start(ActionEvent actionEvent) {
        if (addressInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Podaj adres");
            alert.setHeaderText("Błąd");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
        } else {
            Call<ResponseBody> call = Main.getRestService().startRadio(Main.getToken(), new StationPathDTO(addressInput.getText().toString()));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        refreshCurrentPlayedStationLabel();
                    } else {
                        System.out.println(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
            });
        }
    }

    public void stop(ActionEvent actionEvent) {
        Call<ResponseBody> call = Main.getRestService().stopRadio(Main.getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    refreshCurrentPlayedStationLabel();
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });

    }

    public void add(ActionEvent actionEvent) {
        if (nameInput.getText().isEmpty() || addressInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nie wypełniłeś danych");
            alert.setHeaderText("Błąd");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
        } else {
            Call<StationDTO> call = Main.getRestService().addStation(Main.getToken(), new StationDTO(nameInput.getText().toString(), addressInput.getText().toString()));
            call.enqueue(new Callback<StationDTO>() {
                @Override
                public void onResponse(Call<StationDTO> call, Response<StationDTO> response) {
                    if (response.isSuccessful()) {
                        stations.add(response.body());
                    } else {
                        System.out.println(response.body());
                    }
                }

                @Override
                public void onFailure(Call<StationDTO> call, Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
            });
        }
    }

    public void delete(ActionEvent actionEvent) {
        Call<StationDTO> call = Main.getRestService().deleteStation(Main.getToken(), stations.get(table.getSelectionModel().getFocusedIndex()).getId());
        call.enqueue(new Callback<StationDTO>() {
            @Override
            public void onResponse(Call<StationDTO> call, Response<StationDTO> response) {
                if (response.isSuccessful()) {
                    stations.remove(table.getSelectionModel().getFocusedIndex());
                } else {
                    System.out.println(response.body());
                }
            }

            @Override
            public void onFailure(Call<StationDTO> call, Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
    }

    private void refreshCurrentPlayedStationLabel() {
        Call<StationDTO> currentStation = Main.restService.getCurrentStation(Main.getToken());
        currentStation.enqueue(new Callback<StationDTO>() {
            @Override
            public void onResponse(Call<StationDTO> call, Response<StationDTO> response) {
                if (response.isSuccessful()) {
                    StationDTO current = response.body();
                    Platform.runLater(() -> {
                        if (current.getPath() == null || current.getPath().isEmpty()) {
                            currentPlayedPath.setText("Nic nie jest odtwarzane");
                        } else {
                            currentPlayedPath.setText(current.getPath());
                        }
                    });
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<StationDTO> call, Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
    }


}
