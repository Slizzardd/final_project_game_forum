package ua.com.alevel.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.com.alevel.facade.ImageFacade;

@RequestMapping("/admin")
@RestController
public class AdminRestController {

    private final ImageFacade imageFacade;

    public AdminRestController(ImageFacade imageFacade) {
        this.imageFacade = imageFacade;
    }

    @DeleteMapping("/deleteImage")
    @PreAuthorize("hasAuthority('developers:write')")
    public void deleteImage(@RequestParam("id") long id) {
        imageFacade.delete(id);
    }

    @PostMapping("/approvedImage")
    @PreAuthorize("hasAuthority('developers:write')")
    public void approvedImage(@RequestParam("id") int id) {
        imageFacade.update(id);
    }
}
