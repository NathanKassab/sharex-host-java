package me.bannockhost.bannockhost.account;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class AccountModel {

    @Value("${bannockhost.defaultUrl}")
    private String defaultUrl;

    /**
     * Constructor for ModelAccount
     * @param username The username for the user's account
     * @param email The email for the user's account
     * @param password The password for the user's account
     * @param enabled Whether the user's account is enabled or not
     * @param inviter The user's inviter's ID
     */
    public AccountModel(String username, String email, String password, boolean enabled, Long inviter) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.inviter = inviter;
        this.roles = new ArrayList<>(List.of("ROLE_USER"));
        this.embedTitle = "changeit";
        this.embedDescription = "changeit";
        this.embedUrl = defaultUrl;
        this.embedColor = "#d58cff";
        this.unusedInvites = 0;
    }

    public AccountModel() {}

    @Column(name = "uid", unique = true)
    @GeneratedValue
    @Id
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "account_roles", joinColumns = @JoinColumn(name = "uid"))
    private List<String> roles;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "inviter")
    private Long inviter;

    @Column(name = "last_upload_time")
    private Long lastUpload;

    @Column(name = "upload_key", unique = true)
    private String uploadKey;

    // Sharex embed settings
    @Column(name = "embed_title")
    private String embedTitle;
    @Column(name = "embed_description")
    private String embedDescription;
    @Column(name = "embed_color")
    private String embedColor;
    @Column(name = "embed_url")
    private String embedUrl;

    @Column(name = "unused_invites")
    private int unusedInvites;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> role) {
        this.roles = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getInviter() {
        return inviter;
    }

    public void setInviter(Long inviter) {
        this.inviter = inviter;
    }

    public Long getLastUpload() {
        return lastUpload;
    }

    public void setLastUpload(Long lastUpload) {
        this.lastUpload = lastUpload;
    }

    public String getUploadKey() {
        return uploadKey;
    }

    public void setUploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
    }

    public String getEmbedTitle() {
        return embedTitle;
    }

    public void setEmbedTitle(String embedTitle) {
        this.embedTitle = embedTitle;
    }

    public String getEmbedDescription() {
        return embedDescription;
    }

    public void setEmbedDescription(String embedDescription) {
        this.embedDescription = embedDescription;
    }

    public String getEmbedColor() {
        return embedColor;
    }

    public void setEmbedColor(String embedColor) {
        this.embedColor = embedColor;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public int getUnusedInvites() {
        return unusedInvites;
    }

    public void setUnusedInvites(int unusedInvites) {
        this.unusedInvites = unusedInvites;
    }

}
