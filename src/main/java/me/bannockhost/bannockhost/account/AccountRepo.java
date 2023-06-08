package me.bannockhost.bannockhost.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AccountRepo extends JpaRepository<AccountModel, Long> {

    Optional<AccountModel> findByEmailIgnoreCase(String email);

    Optional<AccountModel> findByUploadKey(String uploadKey);

    boolean existsByIdAndUploadKey(Long id, String uploadKey);

    Optional<AccountModel> findByUsernameIgnoreCase(String username);

}
