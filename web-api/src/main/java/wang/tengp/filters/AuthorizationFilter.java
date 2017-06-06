package wang.tengp.filters;

import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * 授权过滤器
 * Created by shumin on 16-10-13.
 */
public class AuthorizationFilter implements Filter {

    // Hide constructor
    private AuthorizationFilter() {
    }

    /**
     * Initializes singleton.
     */
    private static final class SingletonHolder {
        private static final AuthorizationFilter INSTANCE = new AuthorizationFilter();
    }

    public static final AuthorizationFilter getInstance() {
        return AuthorizationFilter.SingletonHolder.INSTANCE;
    }


    @Override
    public void handle(Request request, Response response) throws Exception {
    }

}
