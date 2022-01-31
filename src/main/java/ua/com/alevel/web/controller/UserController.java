package ua.com.alevel.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.facade.ImageFacade;
import ua.com.alevel.logger.InjectLog;
import ua.com.alevel.logger.LoggerLevel;
import ua.com.alevel.logger.LoggerService;
import ua.com.alevel.persistence.type.Role;
import ua.com.alevel.util.WebRequestUtil;
import ua.com.alevel.web.dto.response.ImageResponseDto;
import ua.com.alevel.web.dto.response.PageData;
import ua.com.alevel.web.dto.response.UserResponseDto;

@Controller
@RequestMapping
public class UserController {

    @InjectLog
    private final LoggerService loggerService;
    private final UserFacade userFacade;
    private final ImageFacade imageFacade;

    public UserController(LoggerService loggerService, UserFacade customerFacade, ImageFacade imageFacade) {
        this.loggerService = loggerService;
        this.userFacade = customerFacade;
        this.imageFacade = imageFacade;
    }

    @GetMapping("/lk.html")
    @PreAuthorize("hasAuthority('developers:read')")
    public String getUser(Model model, WebRequest request) {
        PageData<ImageResponseDto> pageData;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (request.getParameterMap().get(WebRequestUtil.SEARCH_IMAGE_PARAM) != null) {
                pageData = imageFacade.search(request, authentication.getName());
            } else {
                pageData = imageFacade.findAllByUser(request, authentication.getName());
            }
            UserResponseDto user = userFacade.findByEmail(authentication.getName());
            model.addAttribute("isLogin", true);
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);
            model.addAttribute("images", pageData);
            loggerService.commit(LoggerLevel.INFO, "USER WITH EMAIL:" +  user.getEmail() + " LOGGED IN");

            return "../static/lk";
        }
        return null;
    }

    @PostMapping("/searchByUser")
    @PreAuthorize("hasAuthority('developers:read')")
    public String findAllImages(@RequestParam String query, RedirectAttributes attributes) {
        attributes.addAttribute(WebRequestUtil.SEARCH_IMAGE_PARAM, query);
        return "redirect:/lk.html";
    }

    @GetMapping("/active_account/{charToActivations}")
    public String activeAccount(@PathVariable String charToActivations) {
        userFacade.activationAccount(charToActivations);
        return "redirect:/";
    }
}
