package me.bannockhost.bannockhost.upload;

import me.bannockhost.bannockhost.account.AccountModel;
import me.bannockhost.bannockhost.account.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    public UploadServiceImpl(AccountRepo accountRepo, UploadMetadataRepo uploadMetadataRepo){
        this.accountRepo = accountRepo;
        this.uploadMetadataRepo = uploadMetadataRepo;
    }

    private final AccountRepo accountRepo;
    private final UploadMetadataRepo uploadMetadataRepo;

    @Value("${bannockhost.uploads}")
    private String uploadPath;

    @Override
    public String upload(MultipartFile file, AccountModel account) throws IOException {

        // TODO: Validate that the uploaded file is either an image or text file

        // Get the extension of the file
        if (file.getOriginalFilename() == null)
            throw new RuntimeException("File name cannot be null");
        String extension = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        // Generate a new post id
        String postId = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);

        // Upload file
        File image = new File(uploadPath + "/" + account.getId() + "/"
                + postId + "." + extension);
        File dir = new File(uploadPath + "/" + account.getId() + "/");
        dir.mkdirs(); // Make dirs if they don't exist
        file.transferTo(image);

        // Create new metadata record for the upload
        UploadMetadataModel record = new UploadMetadataModel(account.getId(), postId + "." + extension);
        uploadMetadataRepo.save(record);

        return postId + "." + extension;
    }

    @Override
    public byte[] getImage(String username, String postId) throws IOException {
        Optional<AccountModel> account = accountRepo.findByUsernameIgnoreCase(username);
        if (account.isEmpty())
            return null;
        File image = new File(uploadPath + "/" + account.get().getId() + "/"
                + postId);
        // Get bytes from the image file
        return java.nio.file.Files.readAllBytes(image.toPath());
    }

    @Override
    public ArrayList<UploadMetadataModel> getUserGalleryMetadata(AccountModel account, int page) {
        return uploadMetadataRepo.
                findByUploaderIdOrderByUploadTimestampDesc(account.getId(), PageRequest.of(page, 50));
    }

    @Override
    public Optional<UploadMetadataModel> getUploadMetadata(String username, String postId) {
        // First get the id for the username
        Optional<AccountModel> account = accountRepo.findByUsernameIgnoreCase(username);
        if (account.isEmpty())
            return Optional.empty();
        // Then get the metadata for the upload
        return uploadMetadataRepo.findByUploaderIdAndFileId(account.get().getId(), postId);
    }

    @Override
    public int getUserUploadCount(AccountModel account) {
        return uploadMetadataRepo.countAllByUploaderId(account.getId());
    }

    @Override
    public boolean deleteUpload(String username, String postId) throws IOException {
        // First get the id for the username
        Optional<AccountModel> account = accountRepo.findByUsernameIgnoreCase(username);
        if (account.isEmpty())
            return false;

        // Clear metadata for the image from the database
        Optional<UploadMetadataModel> uploadMetadata = uploadMetadataRepo.findByUploaderIdAndFileId(account.get().getId(), postId);
        if (uploadMetadata.isEmpty())
            return false;
        uploadMetadataRepo.delete(uploadMetadata.get());

        // Remove the file from the server
        File image = new File(uploadPath + "/" + account.get().getId() + "/"
                + postId);
        return image.delete();
    }
}
