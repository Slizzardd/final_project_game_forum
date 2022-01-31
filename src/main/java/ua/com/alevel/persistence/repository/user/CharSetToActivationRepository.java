package ua.com.alevel.persistence.repository.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.alevel.persistence.entity.user.CharSetToActivation;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.repository.BaseRepository;

@Repository
public interface CharSetToActivationRepository extends BaseRepository<CharSetToActivation> {

    CharSetToActivation findCharSetToActivationByString(String string);

    @Transactional
    @Modifying
    @Query(value = "delete from CharSetToActivation с where с.id = :id")
    void delete(@Param("id") Long id);
}