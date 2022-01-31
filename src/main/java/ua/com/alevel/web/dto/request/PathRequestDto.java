package ua.com.alevel.web.dto.request;

public class PathRequestDto extends RequestDto{

    private String pathToSmall;
    private String pathToBig;

    public PathRequestDto() {
    }

    public String getPathToSmall() {
        return pathToSmall;
    }

    public void setPathToSmall(String pathToSmall) {
        this.pathToSmall = pathToSmall;
    }

    public String getPathToBig() {
        return pathToBig;
    }

    public void setPathToBig(String pathToBig) {
        this.pathToBig = pathToBig;
    }
}
