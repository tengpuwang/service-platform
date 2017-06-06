package wang.tengp.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.Document;
import wang.tengp.core.BaseDocument;
import wang.tengp.core.annotation.ActiveRecord;

import java.util.Date;

/**
 * 上传文件
 * Created by shumin on 16-9-26.
 */
@ActiveRecord
@Scope("prototype")
@Document(collection = "file")
public class File extends BaseDocument<File> {

    private String fileName;
    private String postfix;
    private String contentType;
    private Long sizeInBytes;

    private String path;
    private String hash;
    private String token;
    private String key;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    public File() {
    }

    public String getFileName() {
        return fileName;
    }

    public File setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getPostfix() {
        return postfix;
    }

    public File setPostfix(String postfix) {
        this.postfix = postfix;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public File setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Long getSizeInBytes() {
        return sizeInBytes;
    }

    public File setSizeInBytes(Long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
        return this;
    }

    public String getPath() {
        return path;
    }

    public File setPath(String path) {
        this.path = path;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public File setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getToken() {
        return token;
    }

    public File setToken(String token) {
        this.token = token;
        return this;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public File setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public String getKey() {
        return key;
    }

    public File setKey(String key) {
        this.key = key;
        return this;
    }
}
