package me.bannockhost.bannockhost.upload;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "upload_metadata")
public class UploadMetadataModel {

    /**
     * @param uploaderId The ID of the user who uploaded the file
     * @param fileId The ID of the file
     */
    public UploadMetadataModel(long uploaderId, String fileId) {
        this.uploaderId = uploaderId;
        this.fileId = fileId;
        this.uploadTimestamp = System.currentTimeMillis();
    }

    public UploadMetadataModel(){}

    @Column(name = "id")
    @GeneratedValue
    @Id
    private long id;

    @Column(name = "uploaderId")
    private long uploaderId;

    @Column(name = "fileId")
    private String fileId;

    @Column(name = "upload_timestamp")
    private long uploadTimestamp;

    public long getId() {
        return id;
    }

    public long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getFileId() {
        return fileId;
    }

    public long getUploadTimestamp() {
        return uploadTimestamp;
    }

}
