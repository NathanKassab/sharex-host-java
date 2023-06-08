package me.bannockhost.bannockhost.account;

import me.bannockhost.bannockhost.invite.InviteModel;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    /**
     * Creates a new account
     * @param user The username for the account
     * @param email The email for the account
     * @param pass The password for the account
     * @param inviter The inviter's ID
     */
    void createNewAccount(String user, String email, String pass, long inviter);

    /**
     * Refreshes the upload key for an account
     * @param accountModel The account to refresh the upload key for
     */
    void refreshUploadKey(AccountModel accountModel);

    /**
     * Checks if a user with a given uid and upload key exists
     * @param uid The uid to check
     * @param uploadKey The upload key to check
     * @return True if the user exists, otherwise false
     */
    boolean doesUidAndUploadKeyExist(long uid, String uploadKey);

    /**
     * Gets an account by its ID
     * @param id The ID of the account to get
     * @return The account, if it exists
     */
    Optional<AccountModel> getAccountById(long id);

    /**
     * Gets an account by its username
     * @param username The username of the account to get
     * @return The account, if it exists
     */
    Optional<AccountModel> getAccountByUsername(String username);

    /**
     * If an account has unused invites, this method will redeem one and randomly generate an invite code
     * @param accountModel The account to generate an invite for
     * @return The invite code, if one was generated
     */
    Optional<String> requestInviteGeneration(AccountModel accountModel);

    /**
     * Gets an inviter by its invite code
     * @param invite The invite code to get the account by
     * @return The account, if it exists
     */
    Optional<InviteModel> getInvite(String invite);

    /**
     * Consumes an invite
     * @param inviteModel The invite to consume
     */
    void consumeInvite(InviteModel inviteModel);

    /**
     * Gets all invites for an account
     * @param accountModel The account to get invites for
     * @return The invites
     */
    List<InviteModel> getInvitesForAccount(AccountModel accountModel);

}
