package wang.tengp.qiniu;

/**
 * 上传结果 封装
 * Created by shumin on 16-10-27.
 */
public class UploadResult {

    private String hash;
    private String key;
    private String path;
    private String token;

    public UploadResult() {
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
