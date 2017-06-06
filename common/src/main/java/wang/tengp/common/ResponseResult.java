package wang.tengp.common;

import com.alibaba.fastjson.JSON;
import wang.tengp.common.util.ApplicationConextUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应报文
 * Created by shumin on 16/5/18.
 */
public class ResponseResult implements Serializable {

//    //   注册 ObjectId 序列化/反序列化
//    static {
//        System.out.println("注册 ObjectId 序列化/反序列化");
//        SerializeConfig.getGlobalInstance().put(ObjectId.class, ObjectIdSerializer.getInstance());
//        ParserConfig.getGlobalInstance().putDeserializer(ObjectId.class, ObjectIdDeserializer.getInstance());
//    }

    public enum Status {
        OK, ERROR
    }

    private String code;
    private Status status;
    private String message;
    private Object data;
    private Object metadata;
    private Object extra;

    private ResponseResult() {
        super();
    }

    private ResponseResult(String code, Status status, String message, Object data, Object metadata, Object extra) {
        super();
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
        this.metadata = metadata;
        this.extra = extra;
    }

    private ResponseResult(String code, Status status, String message, Object data, Object metadata) {
        super();
        this.code = code;
        this.status = status;
        this.message = message;
        this.metadata = metadata;
        this.data = data;
    }

    private ResponseResult(String code, Status status, String message, Object data) {
        super();
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
//        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
        return JSON.toJSONString(this);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (getCode() != null) {
            map.put("code", getCode());
        }
        if (getStatus() != null) {
            map.put("status", getStatus());
        }
        if (getMessage() != null) {
            map.put("message", getMessage());
        }
        if (getMetadata() != null) {
            map.put("metadata", getMetadata());
        }
        if (getData() != null) {
            map.put("data", getData());
        }
        if (getExtra() != null) {
            map.put("extra", getExtra());
        }
        return map;
    }


    /**
     * @param code     响应状态码
     * @param message  提示信息
     * @param data     响应数据
     * @param metadata 响应元数据
     * @param extra    扩展数据
     * @return
     */
    public static ResponseResult error(String code, String message, Object data, Object metadata, Object extra) {
        return new ResponseResult(code, Status.ERROR, message, data, metadata, extra);
    }

    public static ResponseResult error(String code, Object data, Object metadata, Object extra) {
        String msg = ApplicationConextUtils.getMessageSourceAccessor().getMessage(code);
        return error(code, msg, data, metadata, extra);
    }

    public static ResponseResult error(String code, Object data, Object metadata) {
        return error(code, data, metadata, null);
    }

    /**
     * @param data     数据
     * @param metadata 元数据
     * @return
     */
    public static ResponseResult error(Object data, Object metadata) {
        return ok(ResponseCode.ERROR, data, metadata);
    }

    public static ResponseResult error(String code, Object data) {
        return error(code, data, null, null);
    }

    public static ResponseResult error(Object data) {
        return error(ResponseCode.ERROR, data);
    }

    public static ResponseResult error() {
        return error(ResponseCode.ERROR, null);
    }


    /**
     * @param code     响应状态码
     * @param message  提示信息
     * @param data     响应数据
     * @param metadata 响应元数据
     * @param extra    扩展数据
     * @return
     */
    public static ResponseResult ok(String code, String message, Object data, Object metadata, Object extra) {
        return new ResponseResult(code, Status.OK, message, data, metadata, extra);
    }

    public static ResponseResult ok(String code, Object data, Object metadata, Object extra) {
        String msg = ApplicationConextUtils.getMessageSourceAccessor().getMessage(code);
        return ok(code, msg, data, metadata, extra);
    }

    public static ResponseResult ok(String code, Object data, Object metadata) {
        return ok(code, data, metadata, null);
    }

    /**
     * @param data     数据
     * @param metadata 元数据
     * @return
     */
    public static ResponseResult ok(Object data, Object metadata) {
        return ok(ResponseCode.OK, data, metadata);
    }

    public static ResponseResult ok(String code, Object data) {
        return ok(code, data, null);
    }


    public static ResponseResult ok(Object data) {
        return ok(ResponseCode.OK, data);
    }

    public static ResponseResult ok() {
        return ok(ResponseCode.OK, null);
    }

    //---------------------------------------------------------------------------------------


}