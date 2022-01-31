package ua.com.alevel.web.rest;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ua.com.alevel.facade.ImageFacade;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.web.dto.request.PostRequestDto;

@RestController
public class FileUploadRestController {

    private final ImageFacade imageFacade;
    private final UserFacade userFacade;

    public FileUploadRestController(ImageFacade postFacade, UserFacade userFacade) {
        this.imageFacade = postFacade;
        this.userFacade = userFacade;
    }

    @PostMapping(path = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('developers:read')")
    public String uploadPhoto(@ModelAttribute PostRequestDto req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            req.setEmailUser(authentication.getName());
            imageFacade.create(req);
            return JSONObject.quote("done");
        }
        return null;
    }

    //todo add upload avatar
    @PostMapping(path = "/updateAvatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('developers:read')")
    public String uploadAvatar(@RequestBody MultipartFile multipartFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            userFacade.update(authentication.getName(), multipartFile);
        }
        return null;
    }
}
