package ua.com.alevel.web.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.alevel.facade.ElasticImageSearchFacade;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.facade.ImageFacade;
import ua.com.alevel.persistence.type.Role;
import ua.com.alevel.util.WebRequestUtil;
import ua.com.alevel.web.dto.response.UserResponseDto;
import ua.com.alevel.web.dto.response.ImageResponseDto;
import ua.com.alevel.web.dto.response.PageData;

import java.util.List;

@Controller
@RequestMapping("/")
public class ImageController {

    private final ImageFacade imageFacade;
    private final UserFacade userFacade;
    private final ElasticImageSearchFacade elasticImageSearchFacade;

    public ImageController(ImageFacade imageFacade, UserFacade customerFacade, ElasticImageSearchFacade elasticImageSearchFacade) {
        this.imageFacade = imageFacade;
        this.userFacade = customerFacade;
        this.elasticImageSearchFacade = elasticImageSearchFacade;
    }

    @GetMapping
    public String findAllImages(Model model, WebRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PageData<ImageResponseDto> responseDtoPageData;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (request.getParameterMap().get(WebRequestUtil.SEARCH_IMAGE_PARAM) != null) {
                responseDtoPageData = imageFacade.search(request, authentication.getName());
            } else {
                responseDtoPageData = imageFacade.findAll(request, authentication.getName(), false);
            }
            UserResponseDto user = userFacade.findByEmail(authentication.getName());
            model.addAttribute("isLogin", true);
            model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);
        } else {
            if (request.getParameterMap().get(WebRequestUtil.SEARCH_IMAGE_PARAM) != null) {
                responseDtoPageData = imageFacade.search(request, "");
            } else {
                responseDtoPageData = imageFacade.findAll(request, "", false);
            }
            model.addAttribute("isLogin", false);
            model.addAttribute("isAdmin", false);
        }
        model.addAttribute("images", responseDtoPageData);
        return "../static/index";
    }

    @GetMapping("/suggestions")
    public @ResponseBody
    List<String> findAllSearchImages(@RequestParam String query) {
        return elasticImageSearchFacade.searchImageGameName(query);
    }

    @PostMapping("/search")
    public String findAllImages(@RequestParam String query, RedirectAttributes attributes) {
        attributes.addAttribute(WebRequestUtil.SEARCH_IMAGE_PARAM, query);
        return "redirect:/";
    }
}
