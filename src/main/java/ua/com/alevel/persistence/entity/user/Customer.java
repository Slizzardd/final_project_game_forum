package ua.com.alevel.persistence.entity.user;

import ua.com.alevel.persistence.type.Role;

import javax.persistence.*;
import java.util.Objects;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    public Customer() {
        super();
        setRole(Role.USER);
    }
}
