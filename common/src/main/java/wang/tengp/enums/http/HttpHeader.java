package wang.tengp.enums.http;

/**
 * HTTP 消息头
 * Created by shumin on 16-10-13.
 */
public enum HttpHeader {
/* ------------------------------------------------------------ */
    /**
     * General Fields.
     */
    CONNECTION("Connection"),
    CACHE_CONTROL("Cache-Control"),
    DATE("Date"),
    PRAGMA("Pragma"),
    PROXY_CONNECTION("Proxy-Connection"),
    TRAILER("Trailer"),
    TRANSFER_ENCODING("Transfer-Encoding"),
    UPGRADE("Upgrade"),
    VIA("Via"),
    WARNING("Warning"),
    NEGOTIATE("Negotiate"),

    /* ------------------------------------------------------------ */
    /**
     * Entity Fields.
     */
    ALLOW("Allow"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LANGUAGE("Content-Language"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_LOCATION("Content-Location"),
    CONTENT_MD5("Content-MD5"),
    CONTENT_RANGE("Content-Range"),
    CONTENT_TYPE("Content-Type"),
    EXPIRES("Expires"),
    LAST_MODIFIED("Last-Modified"),

    /* ------------------------------------------------------------ */
    /**
     * Request Fields.
     */
    ACCEPT("Accept"),
    ACCEPT_CHARSET("Accept-Charset"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    AUTHORIZATION("Authorization"),
    EXPECT("Expect"),
    FORWARDED("Forwarded"),
    FROM("From"),
    HOST("Host"),
    IF_MATCH("If-Match"),
    IF_MODIFIED_SINCE("If-Modified-Since"),
    IF_NONE_MATCH("If-None-Match"),
    IF_RANGE("If-Range"),
    IF_UNMODIFIED_SINCE("If-Unmodified-Since"),
    KEEP_ALIVE("Keep-Alive"),
    MAX_FORWARDS("Max-Forwards"),
    PROXY_AUTHORIZATION("Proxy-Authorization"),
    RANGE("Range"),
    REQUEST_RANGE("Request-Range"),
    REFERER("Referer"),
    TE("TE"),
    USER_AGENT("User-Agent"),
    X_FORWARDED_FOR("X-Forwarded-For"),
    X_FORWARDED_PROTO("X-Forwarded-Proto"),
    X_FORWARDED_SERVER("X-Forwarded-Server"),
    X_FORWARDED_HOST("X-Forwarded-Host"),

    /* ------------------------------------------------------------ */
    /**
     * Response Fields.
     */
    ACCEPT_RANGES("Accept-Ranges"),
    AGE("Age"),
    ETAG("ETag"),
    LOCATION("Location"),
    PROXY_AUTHENTICATE("Proxy-Authenticate"),
    RETRY_AFTER("Retry-After"),
    SERVER("Server"),
    SERVLET_ENGINE("Servlet-Engine"),
    VARY("Vary"),
    WWW_AUTHENTICATE("WWW-Authenticate"),

    /* ------------------------------------------------------------ */
    /**
     * Other Fields.
     */
    COOKIE("Cookie"),
    SET_COOKIE("Set-Cookie"),
    SET_COOKIE2("Set-Cookie2"),
    MIME_VERSION("MIME-Version"),
    IDENTITY("identity"),

    X_POWERED_BY("X-Powered-By"),
    HTTP2_SETTINGS("HTTP2-Settings"),

    /* ------------------------------------------------------------ */
    /**
     * HTTP2 Fields.
     */
    C_METHOD(":method"),
    C_SCHEME(":scheme"),
    C_AUTHORITY(":authority"),
    C_PATH(":path"),
    C_STATUS(":status"),

    UNKNOWN("::UNKNOWN::");

    /**
     * CORS 消息头
     */
    public enum CORSHeader {

        // HTTP响应头
        ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
        ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods"),
        ACCESS_CONTROL_ALLOW_HEADERS("Access-Control-Allow-Headers"),

        ACCESS_CONTROL_ALLOW_CREDENTIALS("Access-Control-Allow-Credentials"),

        ACCESS_CONTROL_MAX_AGE("Access-Control-Max-Age"),
        ACCESS_CONTROL_EXPOSE_HEADERS("Access-Control-Expose-Headers"),

        // HTTP 请求头
        ACCESS_CONTROL_REQUEST_METHOD("Access-Control-Request-Method"),
        ACCESS_CONTROL_REQUEST_HEADERS("Access-Control-Request-Headers"),;

        private final String headername;

        private CORSHeader(String headerName) {
            this.headername = headerName;
        }

        public String headername() {
            return headername;
        }
    }

    /**
     * 自定义消息头
     */
    public enum CustomHeader {

        // HTTP响应头
        REQUEST_ID("Request-Id"),
        X_REQUESTED_WITH("X-Requested-With"),
        X_ACCESS_AUTHORITY("X-Access-Authority");

        private final String headername;

        private CustomHeader(String headerName) {
            this.headername = headerName;
        }

        public String headername() {
            return headername;
        }
    }


    private final String headername;

    private HttpHeader(String headerName) {
        this.headername = headerName;
    }

    public String headername() {
        return headername;
    }
}
