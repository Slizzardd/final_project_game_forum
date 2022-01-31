package ua.com.alevel.service.impl;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.logger.InjectLog;
import ua.com.alevel.logger.LoggerLevel;
import ua.com.alevel.logger.LoggerService;
import ua.com.alevel.persistence.crud.CrudRepositoryHelper;
import ua.com.alevel.persistence.datatable.DataTableRequest;
import ua.com.alevel.persistence.datatable.DataTableResponse;
import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.entity.user.CharSetToActivation;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.repository.user.CharSetToActivationRepository;
import ua.com.alevel.persistence.repository.user.UserRepository;
import ua.com.alevel.service.UserService;
import ua.com.alevel.util.mail.SendingMailForActivateAccount;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @InjectLog
    private final LoggerService loggerService;
    private final MailSender mailSender;
    private final UserRepository userRepository;
    private final CharSetToActivationRepository charSetToActivationRepository;
    private final CrudRepositoryHelper<User, UserRepository> crudRepositoryHelper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(LoggerService loggerService, MailSender mailSender, UserRepository userRepository, CharSetToActivationRepository charSetToActivationRepository, CrudRepositoryHelper<User, UserRepository> crudRepositoryHelper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.loggerService = loggerService;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.charSetToActivationRepository = charSetToActivationRepository;
        this.crudRepositoryHelper = crudRepositoryHelper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public void create(User user) {
        if (userRepository.existsByEmail(user.getEmail())
                || userRepository.existsByLogin(user.getLogin())) {
            throw new EntityExistException("this personal is exist");
        }
        loggerService.commit(LoggerLevel.INFO, "CREATED USER(EMAIL)" + user.getEmail());
        //Generation charset to activation
        String charsToActivation = SendingMailForActivateAccount.generationCharToActivationAccount();
        CharSetToActivation charSetToActivation = new CharSetToActivation();
        charSetToActivation.setString(charsToActivation);
        user.setCharSetToActivation(charSetToActivation);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        crudRepositoryHelper.create(userRepository, user);
        sendMailToUser(user.getEmail(), charsToActivation);
    }

    @Override
    public void update(User user) {
        loggerService.commit(LoggerLevel.INFO, "UPDATED USER(USER ID): " + user.getId());
        crudRepositoryHelper.update(userRepository, user);
    }

    @Override
    public void delete(Long id) {
        loggerService.commit(LoggerLevel.INFO, "DELETE USER BY ID: " + id);
        deleteAllRelations(id);
        crudRepositoryHelper.delete(userRepository, id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return crudRepositoryHelper.findById(userRepository, id);
    }

    @Override
    public DataTableResponse<User> findAll(DataTableRequest request) {
        if (MapUtils.isNotEmpty(request.getRequestParamMap())) {
            return crudRepositoryHelper.findAll(userRepository, request, User.class);
        }
        return crudRepositoryHelper.findAll(userRepository, request);
    }

    @Override
    public void activationAccount(String charsToActivations) {
        CharSetToActivation charSetToActivation = charSetToActivationRepository.findCharSetToActivationByString(charsToActivations);
        User user = userRepository.findByCharSetToActivationString(charsToActivations);
        loggerService.commit(LoggerLevel.INFO, "ACTIVATION ACCOUNT FOR USER(USER_ID): " + user.getId());
        user.setEnabled(true);
        user.setCharSetToActivation(null);
        crudRepositoryHelper.update(userRepository, user);
        charSetToActivationRepository.delete(charSetToActivation.getId());
    }

    @Override
    public User findByEmail(String email) {
        loggerService.commit(LoggerLevel.INFO, "FIND USER BY EMAIL: " + email);
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteAllRelations(Long id) {
        loggerService.commit(LoggerLevel.INFO, "DELETE ALL RELATIONS BY USER_ID: " + id);
        try {
            User user = findById(id).orElse(null);
            if (ObjectUtils.isEmpty(user)) {
                return;
            }
            List<Image> listLikes = user.getLikes().stream().toList();
            List<Image> listImages = user.getImages().stream().toList();
            for (Image image : listLikes) {
                user.removeLike(image);
            }
            for (Image image : listImages) {
                user.removeImage(image);
            }
            update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long getLastIndexUser() {
        loggerService.commit(LoggerLevel.INFO, "GET LAST USER INDEX");
        return userRepository.findTopByOrderByIdDesc().getId();
    }

    @Override
    public long count(){
        return userRepository.count();
    }
    private void sendMailToUser(String email, String charsToActivation) {
        loggerService.commit(LoggerLevel.INFO, "SEND MAIL FOR USER(EMAIL):" + email +"CHARS:" + charsToActivation);
        SimpleMailMessage msg = SendingMailForActivateAccount.sendMail(email, charsToActivation);
        mailSender.send(msg);
    }
}
