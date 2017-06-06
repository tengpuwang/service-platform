package wang.tengp.enums.http;

/**
 * HTTP 请求方法
 * Created by shumin on 16-10-13.
 */
public enum HttpMethod {
    GET,
    POST,
    HEAD,
    DELETE,
    PUT,
    OPTIONS,
    TRACE;

    public static final HttpMethod[] ALL = new HttpMethod[]{GET, POST, HEAD, DELETE, PUT, OPTIONS, TRACE};
}
