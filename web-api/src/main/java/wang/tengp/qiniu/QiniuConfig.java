package wang.tengp.qiniu;

/**
 * 七牛云存储配置
 * Created by shumin on 16-10-20.
 */
public class QiniuConfig {

    private String domain;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public QiniuConfig() {
    }

    public String getDomain() {
        return domain;
    }

    public QiniuConfig setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public QiniuConfig setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public QiniuConfig setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getBucketName() {
        return bucketName;
    }

    public QiniuConfig setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }
}
