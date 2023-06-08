package me.bannockhost.bannockhost.invite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepo extends JpaRepository<InviteModel, Long> {

    Optional<InviteModel> findByInvite(String invite);

    List<InviteModel> findInviteModelsByOwnerId(Long ownerId);

}
