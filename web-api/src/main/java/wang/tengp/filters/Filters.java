package wang.tengp.filters;

import com.google.common.base.Strings;
import spark.Filter;

/**
 * Created by shumin on 16-8-25.
 */
public final class Filters {

    public final static CORSFilter CORS = CORSFilter.getInstance();
    public final static ApiVersionFilter ApiVersion = ApiVersionFilter.getInstance();
    public final static RequestIdFilter RequestId = RequestIdFilter.getInstance();
    public final static AuthenticationFilter Authorization = AuthenticationFilter.getInstance();
    public final static Filter Gzip = (request, response) -> {
        // 开启Gzip压缩
        response.header("Content-Encoding", "gzip");
    };

    public final static Filter ContentType = (request, response) -> {
        String ContentType = response.raw().getContentType();
        if (Strings.isNullOrEmpty(ContentType)) {
            response.header("Content-Type", "application/json;charset=utf-8");
        }
    };
}
