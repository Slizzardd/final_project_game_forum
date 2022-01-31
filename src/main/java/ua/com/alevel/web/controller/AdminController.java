package ua.com.alevel.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import ua.com.alevel.facade.ImageFacade;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.web.dto.response.ImageResponseDto;
import ua.com.alevel.web.dto.response.PageData;
import ua.com.alevel.web.dto.response.UserResponseDto;

@Controller
@RequestMapping("/admin.html")
public class AdminController {

    private final ImageFacade imageFacade;
    private final UserFacade userFacade;

    public AdminController(ImageFacade imageFacade, UserFacade userFacade) {
        this.imageFacade = imageFacade;
        this.userFacade = userFacade;
    }

    @RequestMapping
    @PreAuthorize("hasAuthority('developers:write')")
    public String findAll(Model model, WebRequest webRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PageData<ImageResponseDto> imageResponseDtoPageData = null;
        PageData<UserResponseDto> userResponseDtoPageData = null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            imageResponseDtoPageData = imageFacade.findAll(webRequest, "", true);
            userResponseDtoPageData = userFacade.findAll(webRequest);
            model.addAttribute("isLogin", true);
            model.addAttribute("isAdmin", true);
        }
        model.addAttribute("images", imageResponseDtoPageData);
        model.addAttribute("users", userResponseDtoPageData);
        return "../static/admin";
    }
}
