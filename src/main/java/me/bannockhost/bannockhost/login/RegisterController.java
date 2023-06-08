package me.bannockhost.bannockhost.login;

import me.bannockhost.bannockhost.account.AccountService;
import me.bannockhost.bannockhost.invite.InviteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class RegisterController {

    @Autowired
    public RegisterController(AccountService accountService){
        this.accountService = accountService;
    }

    private final AccountService accountService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/processRegister", method = RequestMethod.POST)
    public ResponseEntity<String> processRegister(@RequestParam(name = "username", required = true) String username,
                                          @RequestParam(name = "email", required = true) String email,
                                          @RequestParam(name = "password", required = true) String password,
                                          @RequestParam(name = "password2", required = true) String password2,
                                          @RequestParam(name = "invite", required = true) String invite) {
        // Make sure passwords match
        if (!password.equals(password2))
            return ResponseEntity.badRequest().body("Passwords do not match!");

        // Validate invite code
        Optional<InviteModel> inviteModel = accountService.getInvite(invite);
        if (inviteModel.isEmpty())
            return ResponseEntity.ok("Invalid invite code!");
        long inviter = inviteModel.get().getOwnerId();
        accountService.consumeInvite(inviteModel.get());

        // TODO: Make sure unique fields are unique

        // Creates the new account
        accountService.createNewAccount(username, email, password, inviter);
        return ResponseEntity.ok("Account Created!");
    }

}
