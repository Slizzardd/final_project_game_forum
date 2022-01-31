package ua.com.alevel.service.impl;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import ua.com.alevel.logger.InjectLog;
import ua.com.alevel.logger.LoggerLevel;
import ua.com.alevel.logger.LoggerService;
import ua.com.alevel.persistence.crud.CrudRepositoryHelper;
import ua.com.alevel.persistence.datatable.DataTableRequest;
import ua.com.alevel.persistence.datatable.DataTableResponse;
import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.repository.image.ImageRepository;
import ua.com.alevel.persistence.type.ImageStatus;
import ua.com.alevel.service.ImageService;

import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final CrudRepositoryHelper<Image, ImageRepository> crudRepositoryHelper;

    @InjectLog
    private final LoggerService loggerService;

    public ImageServiceImpl(ImageRepository imageRepository, CrudRepositoryHelper<Image, ImageRepository> crudRepositoryHelper, LoggerService loggerService) {
        this.imageRepository = imageRepository;
        this.crudRepositoryHelper = crudRepositoryHelper;
        this.loggerService = loggerService;
    }

    @Override
    public void create(Image image) {
        loggerService.commit(LoggerLevel.INFO, "CREATE IMAGE(BIG_PATH):" + image.getPathToBigImage());
        crudRepositoryHelper.create(imageRepository, image);
    }

    @Override
    public void update(Image image) {
        loggerService.commit(LoggerLevel.INFO, "UPDATED IMAGE(IMAGE_ID): " + image.getId());
        crudRepositoryHelper.update(imageRepository, image);
    }

    @Override
    public void delete(Long id) {
        loggerService.commit(LoggerLevel.INFO, "DELETE IMAGE(IMAGE_ID): " + id);
        deleteAllRelations(id);
        crudRepositoryHelper.delete(imageRepository, id);
    }

    @Override
    public Optional<Image> findById(Long id) {
        return crudRepositoryHelper.findById(imageRepository, id);
    }

    @Override
    public DataTableResponse<Image> findAll(DataTableRequest request) {
        DataTableResponse<Image> dataTableResponse;
        if (MapUtils.isNotEmpty(request.getRequestParamMap())) {
            dataTableResponse = crudRepositoryHelper.findAll(imageRepository, request, Image.class);
        } else {
            dataTableResponse = crudRepositoryHelper.findAll(imageRepository, request);
        }
        List<Image> imageList = new ArrayList<>();
        for (Image image : dataTableResponse.getItems()) {
            if (image.getStatus() == ImageStatus.APPROVED) {
                imageList.add(image);
            }
        }
        dataTableResponse.setItems(imageList);
        return dataTableResponse;
    }


    @Override
    public DataTableResponse<Image> findAllForAdmin(DataTableRequest request) {
        DataTableResponse<Image> dataTableResponse;
        if (MapUtils.isNotEmpty(request.getRequestParamMap())) {
            dataTableResponse = crudRepositoryHelper.findAll(imageRepository, request, Image.class);
        } else {
            dataTableResponse = crudRepositoryHelper.findAll(imageRepository, request);
        }
        return dataTableResponse;
    }

    //    TODO fix this(add function for find images by user, this is КОСТЫЛЬ
    @Override
    public DataTableResponse<Image> findAllByUser(DataTableRequest request, User user) {
        DataTableResponse<Image> dataTableResponse;
        if (MapUtils.isNotEmpty(request.getRequestParamMap())) {
            dataTableResponse = crudRepositoryHelper.findAll(imageRepository, request, Image.class);
        } else {
            dataTableResponse = crudRepositoryHelper.findAll(imageRepository, request);
        }
        List<Image> imageList = new ArrayList<>();
        for (Image image : dataTableResponse.getItems()) {
            if (image.getUser().equals(user) && image.getStatus() == ImageStatus.APPROVED) {
                imageList.add(image);
            }
        }
        dataTableResponse.setItems(imageList);
        return dataTableResponse;
    }

    @Override
    public void deleteAllRelations(Long id) {
        loggerService.commit(LoggerLevel.INFO, "DELETE ALL RELATION FOR IMAGE");
        Image image = findById(id).orElse(null);
        if (ObjectUtils.isEmpty(image)) {
            loggerService.commit(LoggerLevel.WARN, "THIS IMAGE NOT FOUND");
            return;
        }
        List<User> userList = image.getLikes().stream().toList();
        for (User user : userList) {
            image.removeLike(user);
        }
        update(image);
    }

    @Override
    public List<Image> search(Map<String, String[]> queryMap, User user) {
        if (user == null) {
            if (queryMap.get("nameGame") != null) {
                loggerService.commit(LoggerLevel.INFO, "SEARCH IMAGES BY NAME GAME" + Arrays.toString(queryMap.get("nameGame")));
                String[] searchImage = queryMap.get("nameGame");
                return imageRepository.findByNameGameContaining(searchImage[0]);
            }
        } else {
            if (queryMap.get("nameGame") != null) {
                loggerService.commit(LoggerLevel.INFO, "SEARCH IMAGES BY NAME GAME" + Arrays.toString(queryMap.get("nameGame")) + "AND USER(USER_ID)" + user.getId());
                String[] searchImage = queryMap.get("nameGame");
                return imageRepository.findByNameGameContainingAndUser(searchImage[0], user);
            }
        }
        return imageRepository.findAll();
    }

    @Override
    public Long getLastIndex() {
        loggerService.commit(LoggerLevel.INFO, "GET LAST IMAGE INDEX");
        Long id;
        try {
            id = imageRepository.findTopByOrderByIdDesc().getId();
            return id;
        } catch (NullPointerException e) {
            return 0L;
        }
    }

    @Override
    public Image likes(User user, Long imageId) {
        loggerService.commit(LoggerLevel.INFO, "USER(USER_ID): " + user.getId() + "LIKES POST" + imageId);
        Image image = crudRepositoryHelper.findById(imageRepository, imageId).orElse(null);
        Set<String> userLoginSet = new HashSet<>();
        assert image != null;
        for (User users : image.getLikes()) {
            userLoginSet.add(users.getLogin());
        }
        if (userLoginSet.contains(user.getLogin())) {
            image.removeLike(user);
        } else {
            image.addLike(user);
        }
        crudRepositoryHelper.update(imageRepository, image);

        return image;
    }
}
