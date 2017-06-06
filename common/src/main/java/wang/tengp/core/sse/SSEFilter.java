package wang.tengp.core.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * CORS跨域访问
 * Created by shumin on 16-8-25.
 */
public final class SSEFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(SSEFilter.class);

    // Hide constructor
    private SSEFilter() {
    }

    /**
     * Initializes singleton.
     */
    private static final class SingletonHolder {
        private static final SSEFilter INSTANCE = new SSEFilter();
    }

    public static final SSEFilter getInstance() {
        return SSEFilter.SingletonHolder.INSTANCE;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        response.header("Content-Type", "text/event-stream;charset=utf-8");
        response.header("Cache-Control", "no-cache");
        response.header("Connection", "keep-alive");
        HttpConnectionPoll.addConnection(new HttpConnection(request, response));
    }
}