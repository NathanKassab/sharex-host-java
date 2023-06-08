package me.bannockhost.bannockhost.upload;

import me.bannockhost.bannockhost.account.AccountModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public interface UploadService {

    /**
     * Uploads a file to the server
     * @param file The file to upload
     * @param account The account that is uploading the file
     * @return The id of the uploaded file, including the extension
     * @throws Exception If the file cannot be uploaded
     */
    String upload(MultipartFile file, AccountModel account) throws Exception;

    /**
     * Gets the image from the server
     * @param username The username of the account that uploaded the image
     * @param postId The id of the post
     * @return The image as a byte array, or null if the image wasn't found
     * @throws IOException If the image cannot be read
     */
    byte[] getImage(String username, String postId) throws IOException;

    /**
     * Gets the metadata for multiple images in a user's gallery
     * @param account The user whose gallery we check
     * @param page The page of the gallery to get
     * @return The metadata for the images in the user's gallery
     */
    ArrayList<UploadMetadataModel> getUserGalleryMetadata(AccountModel account, int page);

    /**
     * Gets the metadata for a single image
     * @param username The username of the account that uploaded the image
     * @param postId The id of the post
     * @return The metadata for the image, may be empty if the image wasn't found
     */
    Optional<UploadMetadataModel> getUploadMetadata(String username, String postId);

    /**
     * Gets the amount of uploads a user has
     * @param account The user whose uploads we check
     * @return The amount of uploads the user has
     */
    int getUserUploadCount(AccountModel account);

    /**
     * Deletes an upload
     * @param username The username of the account that uploaded the image
     * @param postId The id of the post
     * @return True if the upload was deleted, false if it wasn't found
     * @throws IOException If the upload cannot be deleted
     */
    boolean deleteUpload(String username, String postId) throws IOException;

}
