package ua.com.alevel.persistence.entity.user;

import ua.com.alevel.persistence.entity.BaseEntity;

import javax.persistence.*;

@Entity
public class CharSetToActivation extends BaseEntity {

    private String string;

    @OneToOne(mappedBy = "charSetToActivation")
    private User user;

    public CharSetToActivation() {
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
