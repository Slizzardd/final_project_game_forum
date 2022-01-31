package ua.com.alevel.facade.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import ua.com.alevel.facade.ImageFacade;
import ua.com.alevel.persistence.datatable.DataTableRequest;
import ua.com.alevel.persistence.datatable.DataTableResponse;
import ua.com.alevel.persistence.entity.image.Image;
import ua.com.alevel.persistence.entity.user.User;
import ua.com.alevel.persistence.type.ImageStatus;
import ua.com.alevel.properties.StaticProperties;
import ua.com.alevel.service.ImageService;
import ua.com.alevel.service.UserService;
import ua.com.alevel.util.WebRequestUtil;
import ua.com.alevel.util.WebResponseUtil;
import ua.com.alevel.util.image.ImageRenderUtil;
import ua.com.alevel.web.dto.request.PathRequestDto;
import ua.com.alevel.web.dto.request.PostRequestDto;
import ua.com.alevel.web.dto.response.ImageResponseDto;
import ua.com.alevel.web.dto.response.PageData;

import java.io.IOException;
import java.util.*;

@Service
public class ImageFacadeImpl implements ImageFacade {

    private final UserService userService;
    private final ImageService imageService;

    public ImageFacadeImpl(UserService customerService, ImageService imageCrudService) {
        this.userService = customerService;
        this.imageService = imageCrudService;
    }

    @Override
    public void create(PostRequestDto req) {
        try {
            User user = userService.findByEmail(req.getEmailUser());
            Image image = new Image();
            long indexOfDB = imageService.getLastIndex() + 1;
            PathRequestDto pathRequestDto = ImageRenderUtil.writeImageToFilesAndGetPath(req.getMultipartFile(), Long.toString(indexOfDB));
            image.setUser(user);
            image.setNameGame(req.getNameGame());
            image.setPathToBigImage(pathRequestDto.getPathToBig());
            image.setPathToSmallImage(pathRequestDto.getPathToSmall());
            imageService.create(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(long id) {
        Image image = imageService.findById(id).orElse(null);
        assert image != null;
        image.setStatus(ImageStatus.APPROVED);
        imageService.update(image);
    }

    @Override
    public void delete(Long id) {
        imageService.delete(id);
    }

    @Override
    public ImageResponseDto findById(Long id, String userEmail) {
        Image image = imageService.findById(id).orElse(null);
        User user = userService.findByEmail(userEmail);
        assert image != null;
        return new ImageResponseDto(image, user);
    }

    @Override
    public PageData<ImageResponseDto> findAll(WebRequest webRequest, String userEmail, boolean isForAdmin) {
        User user = null;
        Map<String, String[]> queryMap = new HashMap<>();
        if (webRequest.getParameterMap().get(WebRequestUtil.SEARCH_IMAGE_PARAM) != null) {
            String[] params = webRequest.getParameterMap().get(WebRequestUtil.SEARCH_IMAGE_PARAM);
            queryMap.put("nameGame", params);
        }
        if (StringUtils.isNotEmpty(userEmail)) {
            user = userService.findByEmail(userEmail);
        }
        List<ImageResponseDto> images = new ArrayList<>();

        DataTableRequest dataTableRequest = WebRequestUtil.initDataTableRequest(webRequest);
        dataTableRequest.setRequestParamMap(queryMap);
        DataTableResponse<Image> tableResponse;
        if (!checkingForImageSorting(webRequest)) {
            dataTableRequest.setSort("created");
        }
        if (isForAdmin) {
            tableResponse = imageService.findAllForAdmin(dataTableRequest);
        } else {
            tableResponse = imageService.findAll(dataTableRequest);
        }
        for (Image image : tableResponse.getItems()) {
            image.setPathToBigImage(image.getPathToBigImage().replaceAll("D:/Game_Forums/src/main/resources/static/", ""));
            image.setPathToSmallImage(image.getPathToSmallImage().replaceAll("D:/Game_Forums/src/main/resources/static/", ""));
            images.add(new ImageResponseDto(image, user));
        }

        PageData<ImageResponseDto> pageData = (PageData<ImageResponseDto>) WebResponseUtil.initPageData(tableResponse);

        pageData.setItems(images);

        return pageData;
    }

    @Override
    public PageData<ImageResponseDto> search(WebRequest webRequest, String userEmail) {
        User user = null;
        if (StringUtils.isNotEmpty(userEmail)) {
            user = userService.findByEmail(userEmail);
        }
        Map<String, String[]> queryMap = new HashMap<>();
        if (webRequest.getParameterMap().get(WebRequestUtil.SEARCH_IMAGE_PARAM) != null) {
            String[] params = webRequest.getParameterMap().get(WebRequestUtil.SEARCH_IMAGE_PARAM);
            queryMap.put("nameGame", params);
        }
        List<Image> images = imageService.search(queryMap, user);
        List<ImageResponseDto> imageResponseDtos = new ArrayList<>();

        for (Image image : images) {
            image.setPathToBigImage(image.getPathToBigImage().replaceAll("D:/Game_Forums/src/main/resources/static/", ""));
            image.setPathToSmallImage(image.getPathToSmallImage().replaceAll("D:/Game_Forums/src/main/resources/static/", ""));
            imageResponseDtos.add(new ImageResponseDto(image, user));
        }

        PageData<ImageResponseDto> pageData = new PageData<>();
        pageData.setItems(imageResponseDtos);

        return pageData;
    }

    @Override
    public String showNextImageForMainPage(WebRequest webRequest, int numImages, String userEmail) {
        User user = null;
        if (StringUtils.isNotEmpty(userEmail)) {
            user = userService.findByEmail(userEmail);
        }
        List<ImageResponseDto> images = new ArrayList<>();

        DataTableRequest dataTableRequest = WebRequestUtil.initDataTableRequest(webRequest);
        dataTableRequest.setSize(numImages + 3);
        DataTableResponse<Image> tableResponse = imageService.findAll(dataTableRequest);
        for (Image image : tableResponse.getItems()) {
            image.setPathToBigImage(image.getPathToBigImage().replaceAll(StaticProperties.PATH_PROJECT +"/src/main/resources/static/", ""));
            image.setPathToSmallImage(image.getPathToSmallImage().replaceAll( StaticProperties.PATH_PROJECT + "/src/main/resources/static/", ""));
            images.add(new ImageResponseDto(image, user));
        }

        StringBuilder nextHtml = new StringBuilder();
        for (int i = numImages; i <= tableResponse.getItems().size() - 1; i++) {
//            div
            nextHtml.append('<');
            nextHtml.append("div class=");
            nextHtml.append('"');
            nextHtml.append("post");
            nextHtml.append('"');
            nextHtml.append(" data-item=");
            nextHtml.append('"');
            nextHtml.append(images.get(i).getId());
            nextHtml.append('"');
            nextHtml.append('>');
//            img
            nextHtml.append('<');
            nextHtml.append("img src=");
            nextHtml.append('"');
            nextHtml.append(images.get(i).getPathToSmallImage());
            nextHtml.append('"');
            nextHtml.append(" alt=");
            nextHtml.append('"');
            nextHtml.append(images.get(i).getNameGame());
            nextHtml.append('"');
            nextHtml.append(" data-img=");
            nextHtml.append('"');
            nextHtml.append(images.get(i).getPathToBigImage());
            nextHtml.append('"');
            nextHtml.append('>');
//            Name game
            nextHtml.append('<');
            nextHtml.append("p");
            nextHtml.append('>');
            nextHtml.append(images.get(i).getNameGame());
            nextHtml.append('<');
            nextHtml.append("/p");
            nextHtml.append('>');
//            span
            nextHtml.append('<');
            nextHtml.append("span class=");
            nextHtml.append('"');
            nextHtml.append("like ");
            if (images.get(i).isMeLiked()) {
                nextHtml.append("liked ");
            }

            if (ObjectUtils.isNotEmpty(user)) {
                nextHtml.append("true");
            } else {
                nextHtml.append("false");
            }
            nextHtml.append('"');
            nextHtml.append('>');
            nextHtml.append(images.get(i).getNumLikes());
            nextHtml.append('<');
            nextHtml.append("/span");
            nextHtml.append('>');
//            /div
            nextHtml.append('<');
            nextHtml.append("/div");
            nextHtml.append('>');
        }
        return nextHtml.toString();
    }

    @Override
    public PageData<ImageResponseDto> findAllByUser(WebRequest webRequest, String userEmail) {
        User user = null;
        if (StringUtils.isNotEmpty(userEmail)) {
            user = userService.findByEmail(userEmail);
        }
        DataTableRequest dataTableRequest = WebRequestUtil.initDataTableRequest(webRequest);
        DataTableResponse<Image> tableResponse = imageService.findAllByUser(dataTableRequest, user);
        List<ImageResponseDto> images = new ArrayList<>();

        for (Image image : tableResponse.getItems()) {
            image.setPathToBigImage(image.getPathToBigImage().replaceAll(StaticProperties.PATH_PROJECT +"/src/main/resources/static/", ""));
            image.setPathToSmallImage(image.getPathToSmallImage().replaceAll( StaticProperties.PATH_PROJECT + "/src/main/resources/static/", ""));
            images.add(new ImageResponseDto(image, user));
        }

        PageData<ImageResponseDto> pageData = (PageData<ImageResponseDto>) WebResponseUtil.initPageData(tableResponse);

        pageData.setItems(images);

        return pageData;
    }

    @Override
    public ImageResponseDto likes(String userEmail, Long imageId) {
        User user = userService.findByEmail(userEmail);
        Image image = imageService.likes(user, imageId);

        return new ImageResponseDto(image, user);
    }

    private boolean checkingForImageSorting(WebRequest webRequest) {
        String sort = webRequest.getParameter("sort");
        try {
            return sort.equals("nameGame") || sort.equals("id") || sort.equals("created");
        } catch (NullPointerException e) {
            return true;
        }
    }
}
