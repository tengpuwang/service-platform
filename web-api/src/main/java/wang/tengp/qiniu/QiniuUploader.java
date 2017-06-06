package wang.tengp.qiniu;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import wang.tengp.common.util.ApplicationConextUtils;

import java.io.File;
import java.io.IOException;

/**
 * 七牛文件上传
 * Created by shumin on 16-9-25.
 */
public final class QiniuUploader {

    //设置好账号的ACCESS_KEY和SECRET_KEY
    private static final String ACCESS_KEY = ApplicationConextUtils.getPropertiesValue("qiniu.access_key");
    private static final String SECRET_KEY = ApplicationConextUtils.getPropertiesValue("qiniu.secret_key");
    //要上传的空间
    private static final String BUCKET_NAME = ApplicationConextUtils.getPropertiesValue("qiniu.bucket_name");
    private static final String DOMAIN = ApplicationConextUtils.getPropertiesValue("qiniu.domain");
    //密钥配置
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    // 覆盖上传
//    private final String getUpToken(String key) {
//        //<bucket>:<key>，表示只允许用户上传指定key的文件。在这种格式下文件默认允许“修改”，已存在同名资源则会被本次覆盖。
//        return auth.uploadToken(BUCKET_NAME, key);
//    }

    //设置callbackUrl以及callbackBody，七牛将文件名和文件大小回调给业务服务器
    private static final String getUpToken(String key) {
        StringMap stringMap = new StringMap();
//        stringMap.put("callbackUrl","http://localhost:4567/callback");
//        stringMap.put("callbackBody", "filename=$(fname)&filesize=$(fsize)");
        String token = auth.uploadToken(BUCKET_NAME, key, 3600, stringMap);
        return token;
    }

    public static final UploadResult upload(File file) throws IOException {

        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        //设置断点记录文件保存在指定文件夹或的File对象
        String recordPath = "temp/upload/record";
        //实例化recorder对象
        Recorder recorder = new FileRecorder(recordPath);
        //实例化上传对象，并且传入一个recorder对象
        UploadManager uploadManager = new UploadManager(c);
        String key = file.getName();
        String token = getUpToken(key);
        try {
            //调用put方法上传，这里指定的key和上传策略中的key要一致
            Response res = uploadManager.put(file, key, token);
            String body = res.bodyString();
            JSONObject object = JSONObject.parseObject(body);
            object.put("token", token);
            object.put("path", DOMAIN + key);
            UploadResult uploadResult = object.toJavaObject(UploadResult.class);
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return null;
    }

    public static final UploadResult upload(byte[] file, String key) throws IOException {

        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        //设置断点记录文件保存在指定文件夹或的File对象
        String recordPath = "temp/upload/record";
        //实例化recorder对象
        Recorder recorder = new FileRecorder(recordPath);
        //实例化上传对象，并且传入一个recorder对象
        UploadManager uploadManager = new UploadManager(c);
        String token = getUpToken(key);
        try {
            //调用put方法上传，这里指定的key和上传策略中的key要一致
            Response res = uploadManager.put(file, key, token);
            String body = res.bodyString();
            JSONObject object = JSONObject.parseObject(body);
            object.put("token", token);
            object.put("path", DOMAIN + key);
            UploadResult uploadResult = object.toJavaObject(UploadResult.class);
            return uploadResult;
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return null;
    }

    // 删除文件
    public static final UploadResult delete(String key) {

        Zone z = Zone.autoZone();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(BUCKET_NAME, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
        return null;
    }

    // 批量删除文件
    public static final UploadResult batchDelete(String... keys) {
        Zone z = Zone.autoZone();
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(z);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        batchOperations.addDeleteOp(BUCKET_NAME, keys);
        Response response = null;
        try {
            response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }
}