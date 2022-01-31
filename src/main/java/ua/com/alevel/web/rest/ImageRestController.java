package ua.com.alevel.web.rest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ua.com.alevel.facade.ImageFacade;

@RestController
public class ImageRestController {

    private final ImageFacade imageFacade;

    public ImageRestController(ImageFacade imageFacade) {
        this.imageFacade = imageFacade;
    }

    @PostMapping("/show_next")
    public String showNextImages(WebRequest webRequest, @RequestParam("numImages") int numImages) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return imageFacade.showNextImageForMainPage(webRequest, numImages, authentication.getName());
        }
        return imageFacade.showNextImageForMainPage(webRequest, numImages, "");
    }
}
