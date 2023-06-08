package me.bannockhost.bannockhost.user;

import me.bannockhost.bannockhost.account.AccountService;
import me.bannockhost.bannockhost.security.SqlUserDetails;
import me.bannockhost.bannockhost.upload.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/panel")
@Secured("ROLE_USER")
public class UserPanelController {

    @Autowired
    public UserPanelController(AccountService accountService, UploadService uploadService){
        this.accountService = accountService;
        this.uploadService = uploadService;
    }

    private final AccountService accountService;
    private final UploadService uploadService;

    @GetMapping
    public String userPanel(Model model,
                            @AuthenticationPrincipal SqlUserDetails userDetails) {
        model.addAttribute("user", userDetails.getAccountObj());
        model.addAttribute("uploadCount", uploadService.getUserUploadCount(userDetails.getAccountObj()));
        return "panel/userPanel";
    }

    @GetMapping("/gallery")
    public String userGallery(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                              Model model,
                              @AuthenticationPrincipal SqlUserDetails userDetails) {
        model.addAttribute("user", userDetails.getAccountObj());
        model.addAttribute("metadataRecords",
                uploadService.getUserGalleryMetadata(userDetails.getAccountObj(), page));
        model.addAttribute("page", page);
        return "panel/userGallery";
    }

    @GetMapping("/invites")
    public String userInvites(Model model,
                              @AuthenticationPrincipal SqlUserDetails userDetails){
        model.addAttribute("invites", accountService.getInvitesForAccount(userDetails.getAccountObj()));
        return "panel/userInvites";
    }

    @PostMapping("/refreshUploadKey")
    public String refreshToken(@AuthenticationPrincipal SqlUserDetails userDetails) {
        accountService.refreshUploadKey(userDetails.getAccountObj());
        return "redirect:/panel";
    }

    @PostMapping("/createInvite")
    public String createInvite(@AuthenticationPrincipal SqlUserDetails userDetails) {
        accountService.requestInviteGeneration(userDetails.getAccountObj());
        return "redirect:/panel";
    }

}
