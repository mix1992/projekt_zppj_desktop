package radio.rest.DTO;

/**
 * Created by bartek on 28.01.17.
 */
public class StationPathDTO {

    private String path;

    public StationPathDTO(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
