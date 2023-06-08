package me.bannockhost.bannockhost.security;

import me.bannockhost.bannockhost.account.AccountModel;
import me.bannockhost.bannockhost.account.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    public UserDetailsServiceImpl(AccountRepo accountRepo){
        this.accountRepo = accountRepo;
    }

    private final AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AccountModel> accountModel = accountRepo.findByEmailIgnoreCase(username);
        if (accountModel.isEmpty())
            throw new UsernameNotFoundException("User was not found");
        return new SqlUserDetails(accountModel.get());
    }

}
