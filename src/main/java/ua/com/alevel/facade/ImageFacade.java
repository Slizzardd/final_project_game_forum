package ua.com.alevel.facade;

import org.springframework.web.context.request.WebRequest;
import ua.com.alevel.web.dto.request.PostRequestDto;
import ua.com.alevel.web.dto.response.ImageResponseDto;
import ua.com.alevel.web.dto.response.PageData;

public interface ImageFacade extends CrudFacade<PostRequestDto, ImageResponseDto> {

    void update(long id);

    ImageResponseDto findById(Long id, String userEmail);

    ImageResponseDto likes(String userEmail, Long imageId);

    PageData<ImageResponseDto> findAll(WebRequest webRequest, String userEmail, boolean isForAdmin);

    PageData<ImageResponseDto> findAllByUser(WebRequest webRequest, String userEmail);

    PageData<ImageResponseDto> search(WebRequest webRequest, String userEmail);

    String showNextImageForMainPage(WebRequest webRequest, int numImages, String userEmail);
}
