package ua.com.alevel.service;

import ua.com.alevel.persistence.datatable.DataTableRequest;
import ua.com.alevel.persistence.datatable.DataTableResponse;
import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.entity.user.User;

import java.util.List;
import java.util.Map;

public interface ImageService extends BaseService<Image> {

    Long getLastIndex();

    Image likes(User user, Long imageId);

    DataTableResponse<Image> findAllByUser(DataTableRequest request, User user);

    void deleteAllRelations(Long id);

    List<Image> search(Map<String, String[]> queryMap, User user);

    DataTableResponse<Image> findAllForAdmin(DataTableRequest request);
}
