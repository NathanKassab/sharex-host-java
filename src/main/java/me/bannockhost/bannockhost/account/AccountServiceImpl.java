package me.bannockhost.bannockhost.account;

import me.bannockhost.bannockhost.invite.InviteModel;
import me.bannockhost.bannockhost.invite.InviteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepo, InviteRepo inviteRepo, PasswordEncoder passwordEncoder){
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.inviteRepo = inviteRepo;
    }

    private final AccountRepo accountRepo;
    private final InviteRepo inviteRepo;
    private final PasswordEncoder passwordEncoder;

    public void createNewAccount(String user, String email, String pass, long inviter){
        AccountModel accountModel = new AccountModel(user, email, passwordEncoder.encode(pass), true, inviter);
        refreshUploadKey(accountModel);
        accountRepo.save(accountModel);
    }

    public void refreshUploadKey(AccountModel accountModel){
        String apiKey = generateRandomString(128);
        int attempts = 0, maxAttempts = 10;
        while (accountRepo.findByUploadKey(apiKey).isPresent()) {
            apiKey = generateRandomString(128);
            if (++attempts >= maxAttempts)
                throw new RuntimeException("Failed to generate unique upload key after " + attempts + " attempts");
        }
        accountModel.setUploadKey(apiKey);
        accountRepo.save(accountModel);
    }

    /**
     * Generates a random string of the specified length.
     * @param length The length of the string to generate.
     *               Must be greater than 0.
     * @return The generated string.
     */
    private String generateRandomString(int length) {
        if (length <= 0)
            throw new IllegalArgumentException("Length must be greater than 0");
        StringBuilder sb = new StringBuilder();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        for (int i = 0; i < length; i++)
            sb.append(chars[(int) (Math.random() * chars.length)]);
        return sb.toString();
    }

    public boolean doesUidAndUploadKeyExist(long uid, String uploadKey){
        return accountRepo.existsByIdAndUploadKey(uid, uploadKey);
    }

    @Override
    public Optional<AccountModel> getAccountById(long id) {
        return accountRepo.findById(id);
    }

    @Override
    public Optional<AccountModel> getAccountByUsername(String username) {
        return accountRepo.findByUsernameIgnoreCase(username);
    }

    @Override
    public Optional<String> requestInviteGeneration(AccountModel accountModel) {
        if (accountModel.getUnusedInvites() <= 0)
            return Optional.empty();

        // Generate a new invite in the format {uid}-{encodedTime}-{randomString}
        accountModel.setUnusedInvites(accountModel.getUnusedInvites() - 1);
        accountRepo.save(accountModel);
        String invite = accountModel.getId() + "-" +
                Long.toString(System.currentTimeMillis(), Character.MAX_RADIX) + "-" +
                generateRandomString(16);
        InviteModel inviteModel = new InviteModel(accountModel.getId(), invite);
        inviteRepo.save(inviteModel);
        return Optional.of(invite);
    }

    @Override
    public Optional<InviteModel> getInvite(String invite) {
        return inviteRepo.findByInvite(invite);
    }

    @Override
    public void consumeInvite(InviteModel inviteModel) {
        inviteRepo.delete(inviteModel);
    }

    @Override
    public List<InviteModel> getInvitesForAccount(AccountModel accountModel) {
        return inviteRepo.findInviteModelsByOwnerId(accountModel.getId());
    }

}
