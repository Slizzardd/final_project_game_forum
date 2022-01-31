package ua.com.alevel.persistence.repository.image;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.repository.BaseRepository;

import java.util.List;

@Repository
public interface ImageRepository extends BaseRepository<Image> {

    Image findTopByOrderByIdDesc();

    List<Image> findByNameGameContaining(String nameGame);

    List<Image> findByNameGameContainingAndUser(String nameGame, User user);
}
