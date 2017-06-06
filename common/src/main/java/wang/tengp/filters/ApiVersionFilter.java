package wang.tengp.filters;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;
import wang.tengp.common.util.ApplicationConextUtils;

/**
 * RequestId
 * Created by shumin on 16-8-25.
 */
public final class ApiVersionFilter implements Filter {

    private final String version = ApplicationConextUtils.getPropertiesValue("api.version");

    // Hide constructor
    private ApiVersionFilter() {
    }

    /**
     * Initializes singleton.
     */
    private static final class SingletonHolder {
        private static final ApiVersionFilter INSTANCE = new ApiVersionFilter();
    }

    public static final ApiVersionFilter getInstance() {
        return ApiVersionFilter.SingletonHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        String pathInfo = request.pathInfo();
        String contextPath = request.contextPath();
        String servletPath = request.servletPath();
        String api_version = request.headers("Api-Version");
        if (version.equals(api_version)) {
            response.header("Api-Version", version);
            return;
        } else {
            Spark.halt(400, "Api Version Error!");
        }
    }
}

