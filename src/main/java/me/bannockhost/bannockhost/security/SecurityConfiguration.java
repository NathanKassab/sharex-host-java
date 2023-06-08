package me.bannockhost.bannockhost.security;

import me.bannockhost.bannockhost.account.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Autowired
    public SecurityConfiguration(AccountRepo accountRepo){
        this.accountRepo = accountRepo;
    }

    private final AccountRepo accountRepo;

    @Bean
    public DefaultSecurityFilterChain configureHttp(HttpSecurity httpSecurity) throws Exception {

        // Configure which urls an anonymous user can access
        httpSecurity.authorizeHttpRequests(authorizeHttpRequests -> {
            authorizeHttpRequests.
                    requestMatchers("/helloworld/**", "/login*", "/logout*", "/css/**",
                            "/register*", "/processRegister*", "/upload", "/i/**")
                    .permitAll()
                    .anyRequest().authenticated(); // Require authentication for all other requests
        });

        // Configure the login and logout pages
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginProcessingUrl("/processLogin").defaultSuccessUrl("/login?success=true")
                    .failureUrl("/login?success=false")
                    .defaultSuccessUrl("/panel", true)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginPage("/login").permitAll();
        });
        httpSecurity.logout(formLogout -> {
           formLogout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true")
                   .invalidateHttpSession(true).permitAll();
        });

        // Disable csrf for /upload
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> {
            httpSecurityCsrfConfigurer.ignoringRequestMatchers("/upload");
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}
