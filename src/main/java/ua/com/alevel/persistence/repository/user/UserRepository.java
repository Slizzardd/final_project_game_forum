package ua.com.alevel.persistence.repository.user;

import org.springframework.stereotype.Repository;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.repository.BaseRepository;

import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<User> {

    User findByLogin(String login);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    User findByCharSetToActivationString(String string);

    User findTopByOrderByIdDesc();
}
