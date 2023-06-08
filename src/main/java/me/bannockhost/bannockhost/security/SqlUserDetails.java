package me.bannockhost.bannockhost.security;

import me.bannockhost.bannockhost.account.AccountModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqlUserDetails implements UserDetails {

    private final AccountModel accountModel;

    public SqlUserDetails(AccountModel accountModel) {
        if (accountModel == null)
            throw new NullPointerException("AccountModel cannot be null");
        this.accountModel = accountModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : accountModel.getRoles())
            roles.add(new SimpleGrantedAuthority(role));
        return roles;
    }

    @Override
    public String getPassword() {
        return accountModel.getPassword();
    }

    @Override
    public String getUsername() {
        return accountModel.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountModel.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountModel.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return accountModel.isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return accountModel.isEnabled();
    }

    public AccountModel getAccountObj() {
        return accountModel;
    }
}
