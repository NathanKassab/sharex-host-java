package me.bannockhost.bannockhost.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.bannockhost.bannockhost.security.SqlUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/config.sxcu")
@Secured("ROLE_USER")
public class ConfigController {

    @PostMapping
    @ResponseBody
    public Object config(@RequestHeader String host,
                         @AuthenticationPrincipal SqlUserDetails userDetails,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        Map<String, Object> config = new HashMap<>();
        config.put("Version", "13.6.1");
        config.put("Name", "BannockHost");
        config.put("DestinationType", "ImageUploader, FileUploader");
        config.put("RequestMethod", "POST");
        config.put("RequestURL", request.getScheme() + "://" + host + "/upload");
        config.put("Body", "MultipartFormData");
        config.put("Arguments", new HashMap<String, Object>() {{
            put("key", userDetails.getAccountObj().getUploadKey());
            put("uid", userDetails.getAccountObj().getId());
        }});
        config.put("FileFormName", "file");
        config.put("URL", "$json:url$");
        config.put("ThumbnailURL", "$json:thumbnail$");
        config.put("DeletionURL", "$json:deletion$");
        config.put("ErrorMessage", "$json:error$");

        // Set as downloadable attachment
        response.addHeader("Content-Disposition", "attachment; filename=\"BannockHost.sxcu\"");

        return config;
    }

}
