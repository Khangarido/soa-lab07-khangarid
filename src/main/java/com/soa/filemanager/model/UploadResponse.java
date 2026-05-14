package com.soa.filemanager.model;

// Файл хуулсаны дараах хариу объект
public class UploadResponse {
    // DO Spaces дахь нийтэд харагдах URL
    private String url;
    // Хадгалагдсан файлын нэр (timestamp + оригинал нэр)
    private String fileName;
    // Хуулалт амжилттай эсэх
    private boolean success;
    // Алдааны мессеж
    private String message;

    public UploadResponse() {}

    public UploadResponse(String url, String fileName, boolean success, String message) {
        this.url = url;
        this.fileName = fileName;
        this.success = success;
        this.message = message;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
