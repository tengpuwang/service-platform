package wang.tengp.core.sse;

import spark.Filter;
import spark.Request;
import spark.Response;

/**
 * Created by shumin on 16-9-1.
 */
public interface ServerSentEventsHandle extends Filter {
    @Override
    void handle(Request request, Response response) throws Exception;
}