package ua.com.alevel.web.dto.response;

import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.type.Role;
import ua.com.alevel.persistence.type.Status;

import java.util.Comparator;

public class UserResponseDto extends ResponseDto {
    private String email;
    private String login;
    private String avatarUrl;
    private Status status;
    private Role role;
    private int numImages;
    private int numLikes;
    private Long numberLikesReceiver;

    public UserResponseDto(User user) {
        setId(user.getId());
        setCreated(user.getCreated());
        setUpdated(user.getUpdated());
        setVisible(user.getVisible());
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.role = user.getRole();
        this.login = user.getLogin();
        this.avatarUrl = user.getAvatarUrl();
        this.numImages = user.getImages().size();
        this.numLikes = user.getLikes().size();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getNumberLikesReceiver() {
        return numberLikesReceiver;
    }

    public void setNumberLikesReceiver(Long numberLikesReceiver) {
        this.numberLikesReceiver = numberLikesReceiver;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public int getNumImages() {
        return numImages;
    }

    public void setNumImages(int numImages) {
        this.numImages = numImages;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public static Comparator<UserResponseDto> numberLikesReceiverComparator = new Comparator<UserResponseDto>() {
        @Override
        public int compare(UserResponseDto o1, UserResponseDto o2) {
            return (int) (o1.getNumberLikesReceiver() - o2.getNumberLikesReceiver());
        }
    };

    public static Comparator<UserResponseDto> numImagesComparator = new Comparator<UserResponseDto>() {
        @Override
        public int compare(UserResponseDto o1, UserResponseDto o2) {
            return (o1.getNumImages() - o2.getNumImages());
        }
    };
}
