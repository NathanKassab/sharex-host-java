package me.bannockhost.bannockhost.image;

import jakarta.servlet.http.HttpServletRequest;
import me.bannockhost.bannockhost.account.AccountModel;
import me.bannockhost.bannockhost.account.AccountRepo;
import me.bannockhost.bannockhost.account.AccountService;
import me.bannockhost.bannockhost.security.SqlUserDetails;
import me.bannockhost.bannockhost.upload.UploadMetadataModel;
import me.bannockhost.bannockhost.upload.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/i")
@CrossOrigin(origins = "*")
public class ImagesController {

    @Autowired
    public ImagesController(UploadService uploadService, AccountService accountService) {
        this.uploadService = uploadService;
this.accountService = accountService;
    }

    private final UploadService uploadService;
    private final AccountService accountService;

    @GetMapping("/{username}/{postId}")
    public String getImage(Model model,
                           @PathVariable("username") String username,
                           @PathVariable("postId") String postId,
                           HttpServletRequest httpServletRequest) {
        String domain = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() +
                ((httpServletRequest.getServerPort() != 80 && httpServletRequest.getServerPort() != 443) ? ":" + httpServletRequest.getServerPort() : "");
        Optional<AccountModel> postOwner = accountService.getAccountByUsername(username);
        model.addAttribute("postId", postId);
        model.addAttribute("username", username);
        model.addAttribute("rawImagePath", "/i/" + username + "/" + postId + "/raw");
        model.addAttribute("rawImageUrl", domain + httpServletRequest.getRequestURI() + "/raw");
        model.addAttribute("url", domain + httpServletRequest.getRequestURI());
        model.addAttribute("embeddedSite", postOwner.isPresent() ? postOwner.get().getEmbedUrl() : "bannock.host");
        model.addAttribute("color", postOwner.isPresent() ? postOwner.get().getEmbedColor() : "#d58cff");
        return "image";
    }

    @GetMapping(value = "/{username}/{postId}/raw", produces = {"image/png", "image/jpeg"})
    public @ResponseBody byte[] getImageRaw(@PathVariable("username") String username,
                                            @PathVariable("postId") String postId) throws IOException {
        byte[] imageBytes = uploadService.getImage(username, postId);
        if (imageBytes == null){
            return new byte[0];
        }
        return imageBytes;
    }

    @GetMapping(value = "/{username}/{postId}/delete")
    @Secured("ROLE_USER")
    public String deleteImage(@PathVariable("username") String username,
                              @PathVariable("postId") String postId,
                              @AuthenticationPrincipal SqlUserDetails userDetails) throws IOException {
        // Get the metadata for the upload
        Optional<UploadMetadataModel> metadata = uploadService.getUploadMetadata(username, postId);
        if (metadata.isEmpty())
            return "redirect:/panel/gallery";

        // Make sure the logged-in user owns the upload
        if (metadata.get().getUploaderId() != userDetails.getAccountObj().getId())
            return "redirect:/login";

        // If the post exists, and the user who requested deletion owns the post, delete it
        uploadService.deleteUpload(username, postId);

        // Redirect back to gallery
        return "redirect:/panel/gallery";
    }
}
