package wang.tengp.filters;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import spark.Filter;
import spark.Request;
import spark.Response;
import wang.tengp.common.util.ApplicationConextUtils;
import wang.tengp.enums.http.HttpHeader;
import wang.tengp.enums.http.HttpMethod;

/**
 * CORS跨域访问
 * Created by shumin on 16-8-25.
 */
public final class CORSFilter implements Filter {

    // Hide constructor
    private CORSFilter() {
    }

    /**
     * Initializes singleton.
     */
    private static final class SingletonHolder {
        private static final CORSFilter INSTANCE = new CORSFilter();
    }

    public static final CORSFilter getInstance() {
        return CORSFilter.SingletonHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {

        //  CORS 跨域请求访问
        response.header(
                HttpHeader.CORSHeader.ACCESS_CONTROL_ALLOW_ORIGIN.headername(),
                ApplicationConextUtils.getPropertiesValue("cors.access-control-allow-origin")
        );
        response.header(
                HttpHeader.CORSHeader.ACCESS_CONTROL_ALLOW_METHODS.headername(),
                ApplicationConextUtils.getPropertiesValue("cors.access-control-allow-methods")
        );
        response.header(
                HttpHeader.CORSHeader.ACCESS_CONTROL_ALLOW_HEADERS.headername(),
                ApplicationConextUtils.getPropertiesValue("cros.access-control-allow-headers")
        );
        response.header(
                HttpHeader.CORSHeader.ACCESS_CONTROL_EXPOSE_HEADERS.headername(),
                ApplicationConextUtils.getPropertiesValue("cros.access-control-expose-headers")
        );

        response.header(
                HttpHeader.CORSHeader.ACCESS_CONTROL_MAX_AGE.headername(),
                ApplicationConextUtils.getPropertiesValue("cors.access-control-max-age")
        );

        // 处理非简单请求的Preflight OPTIONS预请求
        if (request.requestMethod().equals(HttpMethod.OPTIONS.toString())) {
            response.header(
                    HttpHeader.CORSHeader.ACCESS_CONTROL_REQUEST_METHOD.headername(),
                    ApplicationConextUtils.getPropertiesValue("cors.access-control-request-method")
            );
            response.header(HttpHeaders.CONTENT_TYPE, MediaType.PLAIN_TEXT_UTF_8.toString());
            response.body("OK");
            response.status(200);
            return;
        } else {
        }
    }
}