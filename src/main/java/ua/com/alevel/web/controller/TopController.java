package ua.com.alevel.web.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import ua.com.alevel.facade.ImageFacade;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.persistence.type.Role;
import ua.com.alevel.web.dto.response.ImageResponseDto;
import ua.com.alevel.web.dto.response.PageData;
import ua.com.alevel.web.dto.response.UserResponseDto;

@Controller
@RequestMapping("/top.html")
public class TopController {


    private final ImageFacade imageFacade;
    private final UserFacade userFacade;

    public TopController(ImageFacade imageFacade, UserFacade userFacade) {
        this.imageFacade = imageFacade;
        this.userFacade = userFacade;
    }

    @RequestMapping
    public String getTop(Model model, WebRequest webRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PageData<ImageResponseDto> imageResponseDtoPageData = imageFacade.findAll(webRequest, "", false);
        ;
        PageData<UserResponseDto> userResponseDtoPageData = userFacade.findAll(webRequest);
        ;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserResponseDto user = userFacade.findByEmail(authentication.getName());
            model.addAttribute("isLogin", true);
            model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);
        } else {
            model.addAttribute("isLogin", false);
            model.addAttribute("isAdmin", false);
        }
        model.addAttribute("images", imageResponseDtoPageData);
        model.addAttribute("users", userResponseDtoPageData);
        return "../static/top";
    }
}
