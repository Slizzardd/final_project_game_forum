package ua.com.alevel.facade.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.persistence.datatable.DataTableRequest;
import ua.com.alevel.persistence.datatable.DataTableResponse;
import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.entity.user.Customer;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.service.UserService;
import ua.com.alevel.util.DataTableRequestUtil;
import ua.com.alevel.util.WebRequestUtil;
import ua.com.alevel.web.dto.request.UserRequestDto;
import ua.com.alevel.web.dto.response.PageData;
import ua.com.alevel.web.dto.response.UserResponseDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserFacadeImpl implements UserFacade {
    private final UserService userService;

    public UserFacadeImpl(UserService customerService) {
        this.userService = customerService;
    }

    @Override
    public void create(UserRequestDto customerRequestDto) throws EntityExistException {
        Customer customer = new Customer();
        customer.setEmail(customerRequestDto.getEmail());
        customer.setLogin(customerRequestDto.getLogin());
        customer.setPassword(customerRequestDto.getPassword());
        userService.create(customer);
    }

    @Override
    public void update(String userEmail, MultipartFile multipartFile) {
        User user = userService.findByEmail(userEmail);
        //todo add upload avatar
//        Long indexOfDB = userService.getLastIndexUser() + 1;
//        user.setAvatarUrl(AvatarRenderUtil.writeAvatarToFilesAndGetPath(multipartFile, indexOfDB.toString()));
        userService.update(user);
    }

    @Override
    public void delete(Long id) {
        userService.delete(id);
    }

    @Override
    public UserResponseDto findById(Long id) {
        User user = userService.findById(id).orElse(null);
        assert user != null;
        return new UserResponseDto(user);
    }

    @Override
    public PageData<UserResponseDto> findAll(WebRequest webRequest) {
        DataTableRequest dataTableRequest = WebRequestUtil.initDataTableRequest(webRequest);
        DataTableResponse<User> tableResponse;
        if (!checkingForUserSorting(webRequest)) {
            dataTableRequest.setSort("created");
        }
        tableResponse = userService.findAll(dataTableRequest);
        List<UserResponseDto> allUser = new ArrayList<>();
        for (User user : tableResponse.getItems()) {
            long numberLikesReceiver = 0;
            for (Image image : user.getImages()) {
                numberLikesReceiver += image.getLikes().size();
            }
            user.setAvatarUrl(user.getAvatarUrl().replaceAll("D:/Game_Forums/src/main/resources/static/avatars/", ""));
            UserResponseDto userResponseDto = new UserResponseDto(user);
            userResponseDto.setNumberLikesReceiver(numberLikesReceiver);
            allUser.add(userResponseDto);
        }

        PageData<UserResponseDto> pageData = (PageData<UserResponseDto>) DataTableRequestUtil.initPageData(tableResponse);
        pageData.setItems(allUser);
        return pageData;
    }

    @Override
    public UserResponseDto findByEmail(String userEmail) {
        User user = userService.findByEmail(userEmail);
        long numberLikesReceiver = 0;
        for (Image image : user.getImages()) {
            numberLikesReceiver += image.getLikes().size();
        }
        user.setAvatarUrl(user.getAvatarUrl().replaceAll("D:/Game_Forums/src/main/resources/static/", ""));
        UserResponseDto customerResponseDto = new UserResponseDto(user);
        customerResponseDto.setNumberLikesReceiver(numberLikesReceiver);
        return customerResponseDto;
    }

    @Override
    public void activationAccount(String charsToActivations) {
        userService.activationAccount(charsToActivations);
    }

    private boolean checkingForUserSorting(WebRequest webRequest) {
        String sort = webRequest.getParameter("sort");
        try {
            return sort.equals("login") || sort.equals("role") || sort.equals("status") || sort.equals("email")
                    || sort.equals("id") || sort.equals("created");
        } catch (NullPointerException e) {
            return true;
        }
    }
}
