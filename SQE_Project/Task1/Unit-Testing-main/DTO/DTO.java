package DTO;

import java.time.LocalDate;

public class DTO {
    private int id;
    private String fileName;
    private String content;
    private LocalDate createdAt;
    private LocalDate lastModified;
    private String fileHash;

    public DTO(int id, String fileName, String content, LocalDate createdAt, LocalDate lastModified, String fileHash) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.fileHash = fileHash;
    }
    public DTO(int id, String fileName, String content) {
	    this.id = id;
	    this.fileName = fileName;
	    this.content = content;
	    this.createdAt = LocalDate.now(); 
	    this.lastModified = LocalDate.now();
	    this.fileHash = ""; 
	}
    public DTO(String fileName, String content, LocalDate createdAt, LocalDate lastModified, String fileHash) {
        this.fileName = fileName;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.fileHash = fileHash;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
}