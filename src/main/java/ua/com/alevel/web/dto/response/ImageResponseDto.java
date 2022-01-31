package ua.com.alevel.web.dto.response;

import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.type.ImageStatus;

public class ImageResponseDto extends ResponseDto {

    private final String nameGame;
    private final Long numLikes;
    private final String pathToBigImage;
    private final String pathToSmallImage;
    private ImageStatus status;
    private boolean meLiked;

    public ImageResponseDto(Image image, User user) {
        this.nameGame = image.getNameGame();
        this.numLikes = (long) image.getLikes().size();
        this.pathToBigImage = image.getPathToBigImage();
        this.pathToSmallImage = image.getPathToSmallImage();
        this.status = image.getStatus();
        setMeLiked(user, image);
        setId(image.getId());
        setCreated(image.getCreated());
        setUpdated(image.getUpdated());
        setVisible(image.getVisible());
    }


    private void setMeLiked(User user, Image image) {
        meLiked = image.getLikes().contains(user);
    }

    public String getNameGame() {
        return nameGame;
    }


    public Long getNumLikes() {
        return numLikes;
    }


    public String getPathToBigImage() {
        return pathToBigImage;
    }


    public String getPathToSmallImage() {
        return pathToSmallImage;
    }


    public ImageStatus getStatus() {
        return status;
    }

    public void setStatus(ImageStatus status) {
        this.status = status;
    }

    public boolean isMeLiked() {
        return meLiked;
    }

}
