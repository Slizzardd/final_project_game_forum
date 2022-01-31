package ua.com.alevel.web.rest;

import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.alevel.exception.EntityExistException;
import ua.com.alevel.facade.ImageFacade;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.web.dto.request.UserRequestDto;
import ua.com.alevel.web.dto.response.ImageResponseDto;

@RestController
public class UserRestController {

    private final UserFacade userFacade;
    private final ImageFacade imageFacade;

    public UserRestController(UserFacade customerFacade, ImageFacade imageFacade) {
        this.userFacade = customerFacade;
        this.imageFacade = imageFacade;
    }

    @PostMapping(value = "/add_user")
    public String addUser(@RequestBody UserRequestDto req) {
        try {
            userFacade.create(req);
            return JSONObject.quote("done");
        } catch (EntityExistException e) {
            return JSONObject.quote("this personal is exist");
        }
    }

    @PostMapping("/likes")
    @PreAuthorize("hasAuthority('developers:read')")
    public ImageResponseDto likes(@RequestParam("imageId") Long imageId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            ImageResponseDto image = imageFacade.likes(authentication.getName(), imageId);
            return image;
        }
        return null;
    }
}
