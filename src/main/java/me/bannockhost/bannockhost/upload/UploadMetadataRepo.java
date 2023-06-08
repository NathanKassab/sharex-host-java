package me.bannockhost.bannockhost.upload;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UploadMetadataRepo extends JpaRepository<UploadMetadataModel, Long> {

    ArrayList<UploadMetadataModel> findByUploaderIdOrderByUploadTimestampDesc(long uploaderId, Pageable pageable);

    Optional<UploadMetadataModel> findByUploaderIdAndFileId(long uploaderId, String postId);

    int countAllByUploaderId(long uploaderId);

}
