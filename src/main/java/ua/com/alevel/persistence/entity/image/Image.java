package ua.com.alevel.persistence.entity.image;

import ua.com.alevel.persistence.entity.BaseEntity;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.type.ImageStatus;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "images")
public class Image extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "path_small")
    private String pathToSmallImage;

    @Column(name = "path_big")
    private String pathToBigImage;


    @Column(name = "name_game")
    private String nameGame;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ImageStatus status;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likes;

    public Image() {
        this.status = ImageStatus.UNDER_REVIEW;
    }

    public void addLike(User user) {
        user.getLikes().add(this);
        this.likes.add(user);
    }

    public void removeLike(User user) {
        this.likes.remove(user);
        user.getLikes().remove(this);
    }

    public ImageStatus getStatus() {
        return status;
    }

    public void setStatus(ImageStatus status) {
        this.status = status;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPathToSmallImage() {
        return pathToSmallImage;
    }

    public void setPathToSmallImage(String pathToSmallImage) {
        this.pathToSmallImage = pathToSmallImage;
    }

    public String getPathToBigImage() {
        return pathToBigImage;
    }

    public void setPathToBigImage(String pathToBigImage) {
        this.pathToBigImage = pathToBigImage;
    }

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(pathToSmallImage, image.pathToSmallImage) && Objects.equals(pathToBigImage, image.pathToBigImage) && Objects.equals(nameGame, image.nameGame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathToSmallImage, pathToBigImage, nameGame);
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
