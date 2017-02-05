package radio.rest;

import okhttp3.ResponseBody;
import radio.rest.DTO.LoginResponseMessage;
import radio.rest.DTO.StationDTO;
import radio.rest.DTO.StationPathDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by bartek on 28.01.17.
 */
public interface RestService {

    @GET("core/login")
    Call<LoginResponseMessage> login(@Header("Authorization") String authHeader);

    @GET("radio")
    Call<ResponseBody> stopRadio(@Header("Session-Token") String token);

    @POST("radio")
    Call<ResponseBody> startRadio(@Header("Session-Token") String token, @Body StationPathDTO stationPathDTO);

    @POST("station")
    Call<StationDTO> addStation(@Header("Session-Token") String token, @Body StationDTO stationDTO);

    @GET("station")
    Call<List<StationDTO>> getStations(@Header("Session-Token") String token);

    @DELETE("station/{stationId}")
    Call<StationDTO> deleteStation(@Header("Session-Token") String token, @Path("stationId") Long stationId);

    @GET("radio/currentStation")
    Call<StationDTO> getCurrentStation(@Header("Session-Token") String token);

}
