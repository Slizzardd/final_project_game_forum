package ua.com.alevel.facade;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import ua.com.alevel.web.dto.request.PostRequestDto;
import ua.com.alevel.web.dto.request.UserRequestDto;
import ua.com.alevel.web.dto.response.UserResponseDto;
import ua.com.alevel.web.dto.response.PageData;

public interface UserFacade extends CrudFacade<UserRequestDto, UserResponseDto> {

    UserResponseDto findById(Long id);

    void update(String userEmail, MultipartFile multipartFile);

    void activationAccount(String charsToActivations);

    PageData<UserResponseDto> findAll(WebRequest webRequest);

    UserResponseDto findByEmail(String userEmail);
}
