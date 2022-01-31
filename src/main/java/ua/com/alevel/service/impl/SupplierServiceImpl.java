package ua.com.alevel.service.impl;

import org.springframework.stereotype.Service;
import ua.com.alevel.logger.InjectLog;
import ua.com.alevel.logger.LoggerLevel;
import ua.com.alevel.logger.LoggerService;
import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.repository.image.ImageRepository;
import ua.com.alevel.persistence.repository.user.UserRepository;
import ua.com.alevel.persistence.type.ImageStatus;
import ua.com.alevel.service.ImageService;
import ua.com.alevel.service.SupplierService;
import ua.com.alevel.service.UserService;

import java.util.Date;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    @InjectLog
    private final LoggerService loggerService;

    private final Date now;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final long DAYS_TO_DELETE_IN_MILLISECONDS = 259200000; // 3 days

    public SupplierServiceImpl(LoggerService loggerService, ImageRepository imageRepository, ImageService imageService, UserRepository userRepository, UserService userService) {
        this.loggerService = loggerService;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
        this.userRepository = userRepository;
        this.userService = userService;
        now = new Date();
    }

    @Override
    public void removingUnverifiedImages() {
        List<Image> allImages = imageRepository.findAll();
        for (Image image : allImages) {
            if (((now.getTime() - image.getCreated().getTime()) > DAYS_TO_DELETE_IN_MILLISECONDS)
                    && image.getStatus() == ImageStatus.UNDER_REVIEW) {
                loggerService.commit(LoggerLevel.INFO, "DELETE IMAGES(>3DATS UNDER_REVIEW) ID:" + image.getId());
                imageService.delete(image.getId());
            }
        }
    }

    @Override
    public void removingUnverifiedUsers() {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (((now.getTime() - user.getCreated().getTime()) > DAYS_TO_DELETE_IN_MILLISECONDS) && !user.isEnabled()) {
                loggerService.commit(LoggerLevel.INFO, "DELETE USER(>3DATS BANNED) ID:" + user.getId());
                userService.delete(user.getId());
            }
        }
    }
}
