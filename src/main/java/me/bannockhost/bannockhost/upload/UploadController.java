package me.bannockhost.bannockhost.upload;

import jakarta.servlet.http.HttpServletRequest;
import me.bannockhost.bannockhost.account.AccountModel;
import me.bannockhost.bannockhost.account.AccountRepo;
import me.bannockhost.bannockhost.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    public UploadController(UploadService uploadService, AccountService accountService) {
        this.uploadService = uploadService;
        this.accountService = accountService;
    }

    private final UploadService uploadService;
    private final AccountService accountService;

    @PostMapping
    public Object upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("key") String key,
                         @RequestParam("uid") Long uid,
                         HttpServletRequest httpServletRequest) {
        Map<String, String> response = new HashMap<>();

        // Check the make sure the key and uid are valid
        if (!accountService.doesUidAndUploadKeyExist(uid, key)) {
            response.put("error", "Invalid key or uid");
            return response;
        }

        // Upload file
        Optional<AccountModel> account = accountService.getAccountById(uid);
        if (account.isEmpty()) {
            response.put("error", "Invalid uid");
            return response;
        }
        String postId;
        try {
            postId = uploadService.upload(file, account.get());
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return response;
        }

        // Get url to reach file
        String domain = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() +
                ((httpServletRequest.getServerPort() != 80 && httpServletRequest.getServerPort() != 443) ? ":" + httpServletRequest.getServerPort() : "")
                + "/i/";
        String url = domain + account.get().getUsername() + "/" + postId;
        String deletionUrl = domain + account.get().getUsername() + "/delete/" + postId;

        // Create success response
        response.put("url", url);
        response.put("thumbnail", url);
        response.put("deletion", deletionUrl);
        return response;
    }

}
