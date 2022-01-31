package ua.com.alevel.service;

import ua.com.alevel.persistence.entity.user.User;

public interface UserService extends BaseService<User> {

    void activationAccount(String charsToActivations);

    User findByEmail(String email);

    void deleteAllRelations(Long id);

    Long getLastIndexUser();

    long count();
}
