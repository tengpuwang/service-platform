package wang.tengp.core.sse;

import wang.tengp.filters.CORSFilter;
import wang.tengp.filters.RequestIdFilter;
import spark.Spark;

/**
 * 服务器发送事件（Server-Sent Events，简称SSE）
 * Created by shumin on 16-9-1.
 */
public class ServerSentEvents extends Spark {
    public static void sse(String path, ServerSentEventsHandle handle) {
        before("/sse" + path, RequestIdFilter.getInstance());
        before("/sse" + path, CORSFilter.getInstance());
        before("/sse" + path, SSEFilter.getInstance());
        before("/sse" + path, handle);
    }
}