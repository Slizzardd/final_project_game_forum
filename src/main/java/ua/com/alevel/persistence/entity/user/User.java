package ua.com.alevel.persistence.entity.user;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ua.com.alevel.persistence.entity.BaseEntity;
import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.type.Role;
import ua.com.alevel.persistence.type.Status;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "image_Url")
    private String avatarUrl;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "enabled")
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activation_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CharSetToActivation charSetToActivation;

    @ManyToMany(mappedBy = "likes")
    private Set<Image> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Image> images;

    public User() {
        super();
        this.avatarUrl = "D:/Game_Forums/src/main/resources/static/files/avatars/1/standartAva.jpg";
        this.status = Status.ACTIVE;
        this.enabled = false;
        this.images = new HashSet<>();
    }

    public void removeLike(Image image) {
        this.likes.remove(image);
        image.getLikes().remove(this);
    }

    public Set<Image> getLikes() {
        return likes;
    }

    public void setLikes(Set<Image> likes) {
        this.likes = likes;
    }

    public void addImage(Image image) {
        image.setUser(this);
        this.images.add(image);
    }

    public void removeImage(Image image) {
        this.images.remove(image);
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public CharSetToActivation getCharSetToActivation() {
        return charSetToActivation;
    }

    public void setCharSetToActivation(CharSetToActivation charSetToActivation) {
        this.charSetToActivation = charSetToActivation;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, login, password, role);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
